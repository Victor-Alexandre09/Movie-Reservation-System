package Movie_Reservation_System_App.dto;

import jakarta.validation.constraints.NotBlank;

public class RoleDTO {

    public record Request(
            @NotBlank String name) {
    }

    public record Response(
            Long id,
            String name) {
    }
}
