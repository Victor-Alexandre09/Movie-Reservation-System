package Movie_Reservation_System_App.service;

import Movie_Reservation_System_App.dto.ShowTimeDTO;
import Movie_Reservation_System_App.exception.TheaterNotAvailableException;
import Movie_Reservation_System_App.model.Movie;
import Movie_Reservation_System_App.model.ShowTime;
import Movie_Reservation_System_App.model.Theater;
import Movie_Reservation_System_App.repository.ShowTimeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowTimeService {

    private final ShowTimeRepository showTimeRepository;
    private final MovieService movieService;
    private final TheaterService theaterService;
    private final int timeIntervalBetweenSessionsInMinutes;

    public ShowTimeService(ShowTimeRepository showTimeRepository,
            MovieService movieService,
            TheaterService theaterService,
            @Value("${app.business.time-interval-between-sessions}") int timeIntervalBetweenSessionsInMinutes) {
        this.showTimeRepository = showTimeRepository;
        this.movieService = movieService;
        this.theaterService = theaterService;
        this.timeIntervalBetweenSessionsInMinutes = timeIntervalBetweenSessionsInMinutes;
    }

    @Transactional
    public ShowTime createShowTime(ShowTimeDTO.Request showTimeDto) {

        Movie movie = movieService.getMovie(showTimeDto.movieId());
        Theater theater = theaterService.getTheater(showTimeDto.theaterId());

        OffsetDateTime newShowEndTime = getEndTimeOfShow(showTimeDto.startTime(), movie.getDurationMinutes());

        validateTheaterAvaliabilitiyForNewShow(theater.getId(), showTimeDto.startTime(), newShowEndTime);

        ShowTime showTime = new ShowTime();
        showTime.setPrice(showTimeDto.price());
        showTime.setStartTime(showTimeDto.startTime());
        showTime.setEndTime(newShowEndTime);
        showTime.setMovie(movie);
        showTime.setTheater(theater);

        return showTimeRepository.save(showTime);
    }

    public ShowTime getShowTime(Long id) {
        return showTimeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ShowTime not found for id: " + id));
    }

    public ShowTime getShowTimeWithTheater(Long id) {
        return showTimeRepository.findByIdJoinTheater(id)
                .orElseThrow(() -> new EntityNotFoundException("ShowTime not found for id: " + id));
    }

    public Page<ShowTime> getShowTimeList(Pageable pageable) {
        return showTimeRepository.findAll(pageable);
    }

    public Page<ShowTime> getShowTimesByMovieId(Long movieId, Pageable pageable) {
        movieService.getMovie(movieId);

        return showTimeRepository.findFutureShowsByMovieId(movieId, pageable);
    }

    public Page<ShowTime> getShowTimesByDate(OffsetDateTime date, Pageable pageable) {
        OffsetDateTime startOfDay = date
                .toLocalDate().atStartOfDay(date.getOffset()).toOffsetDateTime();
        OffsetDateTime endOfDay = startOfDay.plusDays(1);
        return showTimeRepository.findShowTimesByDate(startOfDay, endOfDay, pageable);
    }

    @Transactional
    public ShowTime updateShowTime(Long id, ShowTimeDTO.UpdateRequest updateRequestDto) {
        ShowTime show = getShowTime(id);

        if (updateRequestDto.movieId() != null) {
            show.setMovie(movieService.getMovie(updateRequestDto.movieId()));
        }
        if (updateRequestDto.theaterId() != null) {
            show.setTheater(theaterService.getTheater(updateRequestDto.theaterId()));
        }
        if (updateRequestDto.price() != null && updateRequestDto.price().compareTo(BigDecimal.ZERO) > 0) {
            show.setPrice(updateRequestDto.price());
        }
        if (updateRequestDto.startTime() != null) {
            show.setStartTime(updateRequestDto.startTime());
        }

        OffsetDateTime newEndTime = getEndTimeOfShow(show.getStartTime(), show.getMovie().getDurationMinutes());
        show.setEndTime(newEndTime);

        validateTheaterAvaliabilitiyForNewShow(
                show.getTheater().getId(),
                show.getStartTime(),
                show.getEndTime(),
                show.getId());

        return showTimeRepository.save(show);
    }

    public void deleteShowTime(Long id) {
        showTimeRepository.delete(getShowTime(id));
    }

    private void validateTheaterAvaliabilitiyForNewShow(Long theatherId, OffsetDateTime newShowStartTime,
            OffsetDateTime newShowEndTime) {
        this.validateTheaterAvaliabilitiyForNewShow(theatherId, newShowStartTime, newShowEndTime, 0L);
    }

    private void validateTheaterAvaliabilitiyForNewShow(Long theatherId, OffsetDateTime newShowStartTime,
            OffsetDateTime newShowEndTime, Long showTimeIdToExclude) {

        List<ShowTime> conflictingShows = showTimeRepository.findConflictingShows(theatherId, newShowStartTime,
                newShowEndTime, showTimeIdToExclude);

        if (!conflictingShows.isEmpty()) {
            String details = conflictingShows
                    .stream()
                    .map(s -> String.format("ID %d TheatherID %d (%s → %s)",
                            s.getId(),
                            s.getTheater().getId(),
                            s.getStartTime(),
                            s.getEndTime()))
                    .collect(Collectors.joining("\n"));

            throw new TheaterNotAvailableException(
                    "The new show time conflicts with existing show times:\n" + details);
        }
    }

    private OffsetDateTime getEndTimeOfShow(OffsetDateTime startTime, Integer movieDuration) {
        return startTime.plusMinutes(movieDuration + timeIntervalBetweenSessionsInMinutes);
    }
}
