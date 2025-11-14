package Movie_Reservation_System_App.service;

import Movie_Reservation_System_App.dto.showTime.ShowTimeRequestDto;
import Movie_Reservation_System_App.dto.showTime.ShowTimeUpdateRequestDto;
import Movie_Reservation_System_App.exception.TheaterNotAvaliableException;
import Movie_Reservation_System_App.mapper.ShowTimeMapper;
import Movie_Reservation_System_App.model.Movie;
import Movie_Reservation_System_App.model.ShowTime;
import Movie_Reservation_System_App.model.Theater;
import Movie_Reservation_System_App.repository.ShowTimeRepository;
import jakarta.persistence.EntityNotFoundException;
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

    ShowTimeRepository showTimeRepository;
    ShowTimeMapper showTimeMapper;
    MovieService movieService;
    TheaterService theaterService;

    public ShowTimeService(ShowTimeRepository showTimeRepository, ShowTimeMapper showTimeMapper, MovieService movieService, TheaterService theaterService) {
        this.showTimeRepository = showTimeRepository;
        this.showTimeMapper = showTimeMapper;
        this.movieService = movieService;
        this.theaterService = theaterService;
    }

    public final int TimeIntervalBetwenSessionsInMinutes = 30;

    @Transactional
    public ShowTime createShowTime(ShowTimeRequestDto showTimeDto) {

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
                .orElseThrow( () -> new EntityNotFoundException("ShowTime not found by id: " + id));
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
    public ShowTime updateShowTime(Long id, ShowTimeUpdateRequestDto updateRequestDto) {
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
                show.getId()
        );

        return showTimeRepository.save(show);
    }

    public void deleteShowTime(Long id) {
        showTimeRepository.delete(getShowTime(id));
    }

    private void validateTheaterAvaliabilitiyForNewShow(Long theatherId, OffsetDateTime newShowStartTime, OffsetDateTime newShowEndTime) {
        this.validateTheaterAvaliabilitiyForNewShow
                (theatherId, newShowStartTime, newShowEndTime, 0L);
    }

    private void validateTheaterAvaliabilitiyForNewShow(Long theatherId, OffsetDateTime newShowStartTime, OffsetDateTime newShowEndTime, Long showTimeIdToExclude) {

        List<ShowTime> conflictingShows = showTimeRepository.findConflictingShows(theatherId, newShowStartTime, newShowEndTime, showTimeIdToExclude);

        if (!conflictingShows.isEmpty()) {
            String details = conflictingShows
                    .stream()
                    .map(s -> String.format("ID %d TheatherID %d (%s → %s)",
                            s.getId(),
                            s.getTheater().getId(),
                            s.getStartTime(),
                            s.getEndTime()))
                    .collect(Collectors.joining("\n"));

            throw new TheaterNotAvaliableException(
                    "The new show time conflicts with existing show times:\n" + details);
        }
    }

    private OffsetDateTime getEndTimeOfShow(OffsetDateTime startTime, Integer movieDuration) {
        return startTime.plusMinutes(movieDuration + TimeIntervalBetwenSessionsInMinutes);
    }
}




