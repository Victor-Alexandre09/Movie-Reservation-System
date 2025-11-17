package Movie_Reservation_System_App.dto.seat;

public record SeatResponseDto(
    Long id,
    Long theaterId,
    String row,
    Integer number
) {}