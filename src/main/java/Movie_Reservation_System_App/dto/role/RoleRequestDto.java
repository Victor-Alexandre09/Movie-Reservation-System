package Movie_Reservation_System_App.dto.role;

import jakarta.validation.constraints.NotBlank;

public record RoleRequestDto(
        @NotBlank
        String name
) {
}
