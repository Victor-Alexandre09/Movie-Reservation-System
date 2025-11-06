package Movie_Reservation_System_App.dto.theater;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TheaterRequestDto(
        @NotBlank
        String name,
        @NotNull
        Integer capacity
) {
}
