package Movie_Reservation_System_App.dto.genre;

import jakarta.validation.constraints.NotBlank;

public record GenreRequestDto(
        @NotBlank
        String name
) {
}
