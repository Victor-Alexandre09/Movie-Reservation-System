package Movie_Reservation_System_App.dto.reservedSeat;

import Movie_Reservation_System_App.model.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record ReservedSeatResponseDto(
        Long id,
        OffsetDateTime createdAt,
        Long seatId,
        String row,
        Integer number
){
}
