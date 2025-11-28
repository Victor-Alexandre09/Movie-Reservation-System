package Movie_Reservation_System_App.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {
    public record Request(
            @NotBlank String name,
            @NotBlank String password,
            @Email @NotBlank String email) {
    }

    public record Response(
            Long id,
            String name,
            String email) {
    }

    public record UpdateRequest(
            String name,
            String password,
            String email) {
    }
}
