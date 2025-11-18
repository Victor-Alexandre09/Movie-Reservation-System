package Movie_Reservation_System_App.dto.reservedSeat;

import Movie_Reservation_System_App.model.Reservation;
import Movie_Reservation_System_App.model.ReservedSeat;
import Movie_Reservation_System_App.model.ShowTime;
import Movie_Reservation_System_App.model.User;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record ReservedSeatResponseDto(
        Long id,
        OffsetDateTime createdAt,
        User user,
        ShowTime showTime,
        Reservation reservation
){
}
