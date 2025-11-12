package Movie_Reservation_System_App.dto.showTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ShowTimeUpdateRequestDto(
        @Future
        OffsetDateTime startTime,
        @Positive
        BigDecimal price,
        Long movieId,
        Long theaterId
) {
}
