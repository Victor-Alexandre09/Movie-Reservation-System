package Movie_Reservation_System_App.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class ShowTimeDTO {
        public record Request(
                        @NotNull @Future OffsetDateTime startTime,
                        @NotNull @Positive BigDecimal price,
                        @NotNull Long movieId,
                        @NotNull Long theaterId) {
        }

        public record Response(
                        Long id,
                        OffsetDateTime startTime,
                        OffsetDateTime endTime,
                        BigDecimal price,
                        MovieDTO.Response movie,
                        TheaterDTO.Response theater) {
        }

        public record UpdateRequest(
                        @Future OffsetDateTime startTime,
                        @Positive BigDecimal price,
                        Long movieId,
                        Long theaterId) {
        }
}
