package Movie_Reservation_System_App.dto.reservedSeat;

import Movie_Reservation_System_App.model.Seat;
import jakarta.validation.constraints.NotNull;

public record ReservedSeatRequestDto(
        @NotNull
        Seat seat,
        @NotNull
        Long reservationId
) {
}
