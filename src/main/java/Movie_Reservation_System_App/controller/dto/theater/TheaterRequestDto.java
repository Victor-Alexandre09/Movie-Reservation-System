package Movie_Reservation_System_App.controller.dto.role;

import jakarta.validation.constraints.NotBlank;

public record RoleRequestDto(
        @NotBlank
        String name
) {
}
