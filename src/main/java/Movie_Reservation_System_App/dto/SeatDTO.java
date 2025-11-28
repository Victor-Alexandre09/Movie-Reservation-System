package Movie_Reservation_System_App.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SeatDTO {

    public record Request(
            @NotNull Long theaterId,
            @NotBlank String row,
            @NotNull Integer number) {
    }

    public record Response(
            Long id,
            Long theaterId,
            String row,
            Integer number) {
    }
}
