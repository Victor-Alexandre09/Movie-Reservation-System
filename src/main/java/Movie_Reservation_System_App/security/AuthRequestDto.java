package Movie_Reservation_System_App.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequestDto (
        @NotBlank
        @Email
        String email,
        @NotBlank
        String password
) {
}