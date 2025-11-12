package Movie_Reservation_System_App.service;

import Movie_Reservation_System_App.dto.showTime.ShowTimeRequestDto;
import Movie_Reservation_System_App.dto.showTime.ShowTimeUpdateRequestDto;
import Movie_Reservation_System_App.exception.TheatherNotAvaliableException;
import Movie_Reservation_System_App.mapper.ShowTimeMapper;
import Movie_Reservation_System_App.model.Movie;
import Movie_Reservation_System_App.model.ShowTime;
import Movie_Reservation_System_App.model.Theater;
import Movie_Reservation_System_App.repository.ShowTimeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ShowTimeServiceTest {

    @Mock
    private ShowTimeRepository showTimeRepository;

    @Mock
    private MovieService movieService;

    @Mock
    private TheaterService theaterService;

    @Mock
    private ShowTimeMapper showTimeMapper; // Mocked, but not used in this service's logic

    @InjectMocks
    private ShowTimeService showTimeService;

    // Test Data
    private Movie testMovie;
    private Theater testTheater;
    private ShowTime testShowTime;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private final int MOVIE_DURATION = 90;
    private final int TIME_INTERVAL = 30; // From ShowTimeService

    @BeforeEach
    void setUp() {
        // Setup common test data
        testMovie = new Movie();
        testMovie.setId(1L);
        testMovie.setTitle("Test Movie");
        testMovie.setDurationMinutes(MOVIE_DURATION);

        testTheater = new Theater();
        testTheater.setId(1L);
        testTheater.setName("Screen 1");

        startTime = OffsetDateTime.of(2025, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC);

        // End time = startTime + duration + interval
        endTime = startTime.plusMinutes(MOVIE_DURATION + TIME_INTERVAL); // 10:00 + 90min + 30min = 12:00

        testShowTime = new ShowTime();
        testShowTime.setId(1L);
        testShowTime.setMovie(testMovie);
        testShowTime.setTheater(testTheater);
        testShowTime.setStartTime(startTime);
        testShowTime.setEndTime(endTime);
        testShowTime.setPrice(new BigDecimal("15.00"));
    }

    // --- createShowTime Tests ---

    @Test
    void createShowTime_Success() {
        // Given
        ShowTimeRequestDto requestDto = new ShowTimeRequestDto(
                startTime, new BigDecimal("15.00"), 1L, 1L);

        given(movieService.getMovie(1L)).willReturn(testMovie);
        given(theaterService.getTheater(1L)).willReturn(testTheater);

        // No conflicts found (excludeId = 0L)
        given(showTimeRepository.findConflictingShows(eq(1L), eq(startTime), eq(endTime), eq(0L)))
                .willReturn(Optional.empty());

        // Mock the save operation
        given(showTimeRepository.save(any(ShowTime.class))).willAnswer(invocation -> {
            ShowTime savedShow = invocation.getArgument(0);
            savedShow.setId(1L); // Simulate saving and getting an ID
            return savedShow;
        });

        // When
        ShowTime result = showTimeService.createShowTime(requestDto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(startTime, result.getStartTime());
        assertEquals(endTime, result.getEndTime()); // Verify end time calculation
        assertEquals(testMovie, result.getMovie());
        assertEquals(testTheater, result.getTheater());
        verify(showTimeRepository).save(any(ShowTime.class));
    }

    @Test
    void createShowTime_Failure_TheaterNotAvailable() {
        // Given
        ShowTimeRequestDto requestDto = new ShowTimeRequestDto(
                startTime, new BigDecimal("15.00"), 1L, 1L);

        given(movieService.getMovie(1L)).willReturn(testMovie);
        given(theaterService.getTheater(1L)).willReturn(testTheater);

        // Simulate finding a conflicting show
        given(showTimeRepository.findConflictingShows(eq(1L), eq(startTime), eq(endTime), eq(0L)))
                .willReturn(Optional.of(List.of(testShowTime)));

        // When & Then
        TheatherNotAvaliableException exception = assertThrows(
                TheatherNotAvaliableException.class,
                () -> showTimeService.createShowTime(requestDto)
        );

        assertTrue(exception.getMessage().contains("conflicts with existing show times"));
        verify(showTimeRepository, never()).save(any());
    }

    @Test
    void createShowTime_Failure_MovieNotFound() {
        // Given
        ShowTimeRequestDto requestDto = new ShowTimeRequestDto(
                startTime, new BigDecimal("15.00"), 99L, 1L); // Non-existent movie

        given(movieService.getMovie(99L)).willThrow(new EntityNotFoundException("Movie not found"));

        // When & Then
        assertThrows(EntityNotFoundException.class,
                () -> showTimeService.createShowTime(requestDto)
        );
        verify(theaterService, never()).getTheater(any());
        verify(showTimeRepository, never()).save(any());
    }

    // --- getShowTime Tests ---

    @Test
    void getShowTime_Success() {
        // Given
        given(showTimeRepository.findById(1L)).willReturn(Optional.of(testShowTime));

        // When
        ShowTime result = showTimeService.getShowTime(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(testShowTime, result);
    }

    @Test
    void getShowTime_Failure_NotFound() {
        // Given
        given(showTimeRepository.findById(99L)).willReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> showTimeService.getShowTime(99L)
        );
        assertEquals("ShowTime not found by id: 99", exception.getMessage());
    }

    // --- getShowTimeList Tests ---

    @Test
    void getShowTimeList_Success() {
        // Given
        given(showTimeRepository.findAll()).willReturn(List.of(testShowTime));

        // When
        List<ShowTime> results = showTimeService.getShowTimeList();

        // Then
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(testShowTime, results.get(0));
    }

    // --- updateShowTime Tests ---

    @Test
    void updateShowTime_Success_AllFields() {
        // Given
        Long showTimeId = 1L;

        // New data for update
        Movie newMovie = new Movie();
        newMovie.setId(2L);
        newMovie.setDurationMinutes(120); // New duration

        Theater newTheater = new Theater();
        newTheater.setId(2L);

        OffsetDateTime newStartTime = startTime.plusHours(5); // 15:00
        BigDecimal newPrice = new BigDecimal("20.00");

        ShowTimeUpdateRequestDto updateRequestDto = new ShowTimeUpdateRequestDto(
                newStartTime, newPrice, 2L, 2L
        );

        // Expected new end time: 15:00 + 120min + 30min = 17:30
        OffsetDateTime expectedNewEndTime = newStartTime.plusMinutes(120 + TIME_INTERVAL);

        given(showTimeRepository.findById(showTimeId)).willReturn(Optional.of(testShowTime));
        given(movieService.getMovie(2L)).willReturn(newMovie);
        given(theaterService.getTheater(2L)).willReturn(newTheater);

        // No conflicts found, excluding the showtime itself (eq(1L))
        given(showTimeRepository.findConflictingShows(eq(2L), eq(newStartTime), eq(expectedNewEndTime), eq(showTimeId)))
                .willReturn(Optional.empty());

        given(showTimeRepository.save(any(ShowTime.class))).willReturn(testShowTime); // Return the mutated object

        // When
        ShowTime result = showTimeService.updateShowTime(showTimeId, updateRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(newMovie, result.getMovie());
        assertEquals(newTheater, result.getTheater());
        assertEquals(newPrice, result.getPrice());
        assertEquals(newStartTime, result.getStartTime());
        assertEquals(expectedNewEndTime, result.getEndTime()); // Verify end time was recalculated
        verify(showTimeRepository).save(testShowTime);
    }

    @Test
    void updateShowTime_Success_PartialUpdate_PriceOnly() {
        // Given
        Long showTimeId = 1L;
        BigDecimal newPrice = new BigDecimal("25.00");

        // DTO with only price updated
        ShowTimeUpdateRequestDto updateRequestDto = new ShowTimeUpdateRequestDto(
                null, newPrice, null, null
        );

        // End time should be recalculated based on existing start time and movie duration
        // endTime = 10:00 + 90min + 30min = 12:00
        OffsetDateTime expectedEndTime = testShowTime.getStartTime().plusMinutes(
                testShowTime.getMovie().getDurationMinutes() + TIME_INTERVAL
        );

        given(showTimeRepository.findById(showTimeId)).willReturn(Optional.of(testShowTime));

        // No conflicts found, excluding the showtime itself
        given(showTimeRepository.findConflictingShows(
                eq(testShowTime.getTheater().getId()),
                eq(testShowTime.getStartTime()),
                eq(expectedEndTime),
                eq(showTimeId)))
                .willReturn(Optional.empty());

        given(showTimeRepository.save(any(ShowTime.class))).willReturn(testShowTime);

        // When
        ShowTime result = showTimeService.updateShowTime(showTimeId, updateRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(newPrice, result.getPrice()); // Price updated
        assertEquals(testMovie, result.getMovie()); // Movie unchanged
        assertEquals(testTheater, result.getTheater()); // Theater unchanged
        assertEquals(startTime, result.getStartTime()); // StartTime unchanged
        assertEquals(expectedEndTime, result.getEndTime()); // EndTime recalculated (but same value)

        verify(movieService, never()).getMovie(any()); // Verify dependencies not called
        verify(theaterService, never()).getTheater(any());
        verify(showTimeRepository).save(testShowTime);
    }

    @Test
    void updateShowTime_Failure_ConflictOnUpdate() {
        // Given
        Long showTimeId = 1L;
        OffsetDateTime newStartTime = startTime.plusHours(1); // 11:00

        ShowTimeUpdateRequestDto updateRequestDto = new ShowTimeUpdateRequestDto(
                newStartTime, null, null, null
        );

        // 11:00 + 90min + 30min = 13:00
        OffsetDateTime newEndTime = newStartTime.plusMinutes(MOVIE_DURATION + TIME_INTERVAL);

        // A different showtime that conflicts
        ShowTime conflictingShow = new ShowTime();
        conflictingShow.setId(2L); // Different ID
        conflictingShow.setTheater(testTheater);

        given(showTimeRepository.findById(showTimeId)).willReturn(Optional.of(testShowTime));

        // Simulate finding a conflict, excluding the showtime being updated (ID 1)
        given(showTimeRepository.findConflictingShows(
                eq(testTheater.getId()), eq(newStartTime), eq(newEndTime), eq(showTimeId)))
                .willReturn(Optional.of(List.of(conflictingShow)));

        // When & Then
        TheatherNotAvaliableException exception = assertThrows(
                TheatherNotAvaliableException.class,
                () -> showTimeService.updateShowTime(showTimeId, updateRequestDto)
        );

        assertTrue(exception.getMessage().contains("conflicts with existing show times"));
        verify(showTimeRepository, never()).save(any());
    }

    @Test
    void updateShowTime_Failure_ShowTimeNotFound() {
        // Given
        ShowTimeUpdateRequestDto updateRequestDto = new ShowTimeUpdateRequestDto(
                null, new BigDecimal("20.00"), null, null);

        given(showTimeRepository.findById(99L)).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class,
                () -> showTimeService.updateShowTime(99L, updateRequestDto)
        );
        verify(showTimeRepository, never()).save(any());
    }

    // --- deleteShowTime Tests ---

    @Test
    void deleteShowTime_Success() {
        // Given
        given(showTimeRepository.findById(1L)).willReturn(Optional.of(testShowTime));

        // When
        showTimeService.deleteShowTime(1L);

        // Then
        verify(showTimeRepository).delete(testShowTime);
    }

    @Test
    void deleteShowTime_Failure_NotFound() {
        // Given
        given(showTimeRepository.findById(99L)).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class,
                () -> showTimeService.deleteShowTime(99L)
        );
        verify(showTimeRepository, never()).delete(any());
    }
}