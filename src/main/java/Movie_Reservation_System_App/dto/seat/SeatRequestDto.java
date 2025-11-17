package Movie_Reservation_System_App.dto.seat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SeatRequestDto(
    @NotNull
    Long theaterId,
    @NotBlank
    String row,
    @NotNull
    Integer number
) {}