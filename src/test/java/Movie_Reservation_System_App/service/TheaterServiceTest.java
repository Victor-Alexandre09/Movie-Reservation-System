package Movie_Reservation_System_App.service;

import Movie_Reservation_System_App.dto.theater.TheaterUpdateRequestDto;
import Movie_Reservation_System_App.exception.DuplicatedRegisterException;
import Movie_Reservation_System_App.mapper.TheaterMapper;
import Movie_Reservation_System_App.model.Theater;
import Movie_Reservation_System_App.repository.TheaterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TheaterServiceTest {

    @Mock
    private TheaterRepository theaterRepository;

    @Mock
    private TheaterMapper theaterMapper;

    private TheaterService theaterService;

    @BeforeEach
    void setUp() {
        theaterService = new TheaterService(theaterRepository, theaterMapper);
    }

    @Test
    void testCreateTheater_WhenTheaterDoesNotExist_ShouldSaveAndReturnTheater() {
        Theater theater = new Theater();
        theater.setId(1L);
        theater.setName("Main Theater");
        theater.setCapacity(100);

        when(theaterRepository.findByName("Main Theater")).thenReturn(Optional.empty());
        when(theaterRepository.save(theater)).thenReturn(theater);

        Theater createdTheater = theaterService.createTheater(theater);

        assertNotNull(createdTheater);
        assertEquals(1L, createdTheater.getId());
        assertEquals("Main Theater", createdTheater.getName());
        assertEquals(100, createdTheater.getCapacity());
        verify(theaterRepository).findByName("Main Theater");
        verify(theaterRepository).save(theater);
    }

    @Test
    void testCreateTheater_WhenTheaterAlreadyExists_ShouldThrowDuplicatedRegisterException() {
        Theater existingTheater = new Theater();
        existingTheater.setId(1L);
        existingTheater.setName("Main Theater");

        Theater theaterToCreate = new Theater();
        theaterToCreate.setName("Main Theater");

        when(theaterRepository.findByName("Main Theater")).thenReturn(Optional.of(existingTheater));

        DuplicatedRegisterException exception = assertThrows(
                DuplicatedRegisterException.class,
                () -> theaterService.createTheater(theaterToCreate)
        );

        assertEquals("the theater Main Theater already exists", exception.getMessage());
        verify(theaterRepository).findByName("Main Theater");
        verify(theaterRepository, never()).save(any(Theater.class));
    }

    @Test
    void testGetTheater_WhenTheaterExists_ShouldReturnTheater() {
        Theater existingTheater = new Theater();
        existingTheater.setId(1L);
        existingTheater.setName("Main Theater");

        when(theaterRepository.findById(1L)).thenReturn(Optional.of(existingTheater));

        Theater foundTheater = theaterService.getTheater(1L);

        assertNotNull(foundTheater);
        assertEquals(1L, foundTheater.getId());
        verify(theaterRepository).findById(1L);
    }

    @Test
    void testGetTheater_WhenTheaterDoesNotExist_ShouldThrowEntityNotFoundException() {

        Long theaterId = 99L;
        when(theaterRepository.findById(theaterId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> theaterService.getTheater(theaterId)
        );

        assertEquals("theater not found for id: " + theaterId, exception.getMessage());
        verify(theaterRepository).findById(theaterId);
    }

    @Test
    void testGetTheatersList_WhenTheatersExist_ShouldReturnListOfTheaters() {
        Theater theater1 = new Theater();
        theater1.setId(1L);
        theater1.setName("Theater 1");

        Theater theater2 = new Theater();
        theater2.setId(2L);
        theater2.setName("Theater 2");

        List<Theater> theaterList = List.of(theater1, theater2);

        when(theaterRepository.findAll()).thenReturn(theaterList);

        List<Theater> result = theaterService.getTheatersList();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Theater 1", result.get(0).getName());
        assertEquals("Theater 2", result.get(1).getName());
        verify(theaterRepository).findAll();
    }

    @Test
    void testGetTheatersList_WhenNoTheatersExist_ShouldReturnEmptyList() {
        when(theaterRepository.findAll()).thenReturn(Collections.emptyList());

        List<Theater> result = theaterService.getTheatersList();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(theaterRepository).findAll();
    }

    @Test
    void testUpdateTheater_WhenTheaterExists_ShouldUpdateAndReturnTheater() {
        Long theaterId = 1L;

        Theater existingTheater = new Theater();
        existingTheater.setId(theaterId);
        existingTheater.setName("Old Name");
        existingTheater.setCapacity(100);

        TheaterUpdateRequestDto updateDto = new TheaterUpdateRequestDto("New name", 120);

        Theater updatedTheaterMock = new Theater();
        updatedTheaterMock.setId(theaterId);
        updatedTheaterMock.setName("New Name");
        updatedTheaterMock.setCapacity(150);

        when(theaterRepository.findById(theaterId)).thenReturn(Optional.of(existingTheater));
        doNothing().when(theaterMapper).updateTheaterFromDto(updateDto, existingTheater);
        when(theaterRepository.save(existingTheater)).thenReturn(updatedTheaterMock);

        Theater updatedTheater = theaterService.updateTheater(theaterId, updateDto);

        assertNotNull(updatedTheater);
        assertEquals("New Name", updatedTheater.getName());
        assertEquals(150, updatedTheater.getCapacity());
        verify(theaterRepository).findById(theaterId);
        verify(theaterMapper).updateTheaterFromDto(updateDto, existingTheater);
        verify(theaterRepository).save(existingTheater);
    }

    @Test
    void testUpdateTheater_WhenTheaterDoesNotExist_ShouldThrowEntityNotFoundException() {
        Long theaterId = 99L;
        TheaterUpdateRequestDto updateDto = new TheaterUpdateRequestDto("teather", 100);

        when(theaterRepository.findById(theaterId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> theaterService.updateTheater(theaterId, updateDto)
        );

        assertEquals("theater not found for id: 99", exception.getMessage());
        verify(theaterRepository).findById(theaterId);
        verify(theaterMapper, never()).updateTheaterFromDto(any(), any());
        verify(theaterRepository, never()).save(any(Theater.class));
    }

    @Test
    void testDeleteTheater_WhenTheaterExists_ShouldDeleteTheater() {
        Long theaterId = 1L;
        Theater existingTheater = new Theater();
        existingTheater.setId(theaterId);
        existingTheater.setName("To Be Deleted");

        when(theaterRepository.findById(theaterId)).thenReturn(Optional.of(existingTheater));
        doNothing().when(theaterRepository).delete(existingTheater);

        theaterService.deleteTheater(theaterId);

        verify(theaterRepository).findById(theaterId);
        verify(theaterRepository).delete(existingTheater);
    }

    @Test
    void testDeleteTheater_WhenTheaterDoesNotExist_ShouldThrowEntityNotFoundException() {
        Long theaterId = 99L;

        when(theaterRepository.findById(theaterId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> theaterService.deleteTheater(theaterId)
        );

        assertEquals("theater not found for id: " + theaterId, exception.getMessage());
        verify(theaterRepository).findById(theaterId);
        verify(theaterRepository, never()).delete(any(Theater.class));
    }
}