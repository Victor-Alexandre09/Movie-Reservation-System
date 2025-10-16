package Movie_Reservation_System_App.controller.dto.genre;

import jakarta.validation.constraints.NotBlank;

public record GenreResponseDto(
        Long id,
        String name
) {
}
