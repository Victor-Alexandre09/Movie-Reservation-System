package Movie_Reservation_System_App.dto.showTime;

import Movie_Reservation_System_App.dto.movie.MovieResponseDto;
import Movie_Reservation_System_App.dto.theater.TheaterResponseDto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ShowTimeResponseDto(
        Long id,
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        BigDecimal price,
        MovieResponseDto movie,
        TheaterResponseDto theater
) {
}
