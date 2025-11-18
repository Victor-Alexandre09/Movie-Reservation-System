package Movie_Reservation_System_App.dto.reservation;

import Movie_Reservation_System_App.dto.reservedSeat.ReservedSeatResponseDto;
import Movie_Reservation_System_App.dto.theater.TheaterResponseDto;
import Movie_Reservation_System_App.dto.user.UserResponseDto;
import Movie_Reservation_System_App.model.ReservedSeat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record ReservationResponseDto(
        Long id,
        String status,
        OffsetDateTime createdAt,
        BigDecimal totalPrice,
        UserResponseDto user,
        ShowTimeInfo showTime,
        List<ReservedSeatResponseDto> reservedSeats
){
    public record ShowTimeInfo(
            Long id,
            OffsetDateTime startTime,
            BigDecimal price,
            TheaterResponseDto theater
    ) {}
}
