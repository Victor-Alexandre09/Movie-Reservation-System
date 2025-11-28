package Movie_Reservation_System_App.dto;

import jakarta.validation.constraints.NotBlank;

public class TheaterDTO {
    public record Request(
            @NotBlank String name) {
    }

    public record Response(
            Long id,
            String name) {
    }

    public record UpdateRequest(
            String name) {
    }
}
