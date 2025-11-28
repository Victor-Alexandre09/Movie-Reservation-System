package Movie_Reservation_System_App.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;

public class MovieDTO {
    public record Request(
            @NotBlank String title,
            @NotBlank String posterImageUrl,
            @NotNull Integer durationMinutes,
            @NotNull LocalDate releaseDate,
            @NotEmpty Set<Long> genreIds) {
    }

    public record Response(
            Long id,
            String title,
            String posterImageUrl,
            Integer durationMinutes,
            LocalDate releaseDate,
            Set<GenreDTO.Response> genres) {
    }

    public record UpdateRequest(
            String title,
            String posterImageUrl,
            Integer durationMinutes,
            LocalDate releaseDate,
            Set<Long> genreIds) {
    }
}
