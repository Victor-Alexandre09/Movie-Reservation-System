package Movie_Reservation_System_App.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(
        @NotBlank
        String name,
        @NotBlank
        String password,
        @Email
        @NotBlank
        String email

) {
}
