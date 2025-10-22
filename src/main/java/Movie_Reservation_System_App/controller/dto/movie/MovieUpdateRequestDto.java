package Movie_Reservation_System_App.controller.dto.movie;

import java.time.LocalDate;
import java.util.Set;

public record MovieUpdateRequestDto(
        String title,
        String posterImageUrl,
        Integer durationMinutes,
        LocalDate releaseDate,
        Set<Long> genresId
) {
}
