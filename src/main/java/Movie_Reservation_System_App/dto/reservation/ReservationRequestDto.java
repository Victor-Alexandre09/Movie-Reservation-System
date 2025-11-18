package Movie_Reservation_System_App.dto.reservation;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReservationRequestDto(
//        @NotNull
//        @Positive
//        BigDecimal totalrice,
        @NotNull
        Long showTimeId,
        @NotNull
        @NotEmpty
        List<Long> seatId
) {
}
