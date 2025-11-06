package Movie_Reservation_System_App.dto.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;

public record MovieRequestDto(
        @NotBlank
        String title,
        @NotBlank
        String posterImageUrl,
        @NotNull
        Integer durationMinutes,
        @NotNull
        LocalDate releaseDate,
        @NotEmpty
        Set<Long> genreIds
) {
}
