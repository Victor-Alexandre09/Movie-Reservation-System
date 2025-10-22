package Movie_Reservation_System_App.controller.dto.movie;

import Movie_Reservation_System_App.controller.dto.genre.GenreResponseDto;

import java.time.LocalDate;
import java.util.Set;

public record MovieResponseDto(
        Long id,
        String title,
        String posterImageUrl,
        Integer durationMinutes,
        LocalDate releaseDate,
        Set<GenreResponseDto> genres
) {
}