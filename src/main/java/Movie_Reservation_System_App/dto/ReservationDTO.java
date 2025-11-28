package Movie_Reservation_System_App.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class ReservationDTO {

        public record Request(
                        @NotNull Long showTimeId,
                        @NotNull @NotEmpty List<Long> seatId) {
        }

        public record Response(
                        Long id,
                        String status,
                        OffsetDateTime createdAt,
                        BigDecimal totalPrice,
                        UserDTO.Response user,
                        ShowTimeInfo showTime,
                        List<ReservedSeatDTO.Response> reservedSeats) {
                public record ShowTimeInfo(
                                Long id,
                                OffsetDateTime startTime,
                                BigDecimal price,
                                TheaterDTO.Response theater) {
                }
        }
}
