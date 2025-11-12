package Movie_Reservation_System_App.dto.showTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ShowTimeRequestDto(
        @NotNull
        @Future
        OffsetDateTime startTime,
        @NotNull
        @Positive
        BigDecimal price,
        @NotNull
        Long movieId,
        @NotNull
        Long theaterId
) {
}
