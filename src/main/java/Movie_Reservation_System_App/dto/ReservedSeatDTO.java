package Movie_Reservation_System_App.dto;

import Movie_Reservation_System_App.model.Seat;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public class ReservedSeatDTO {

    public record Request(
            @NotNull Seat seat,
            @NotNull Long reservationId) {
    }

    public record Response(
            Long id,
            OffsetDateTime createdAt,
            Long seatId,
            String row,
            Integer number) {
    }
}
