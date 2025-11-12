package Movie_Reservation_System_App.repository;

import Movie_Reservation_System_App.model.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {

    @Query("SELECT s FROM ShowTime s WHERE s.theater.id = :theaterId " +
            "AND s.id != :showTimeIdToExclude " +
            "AND s.startTime <= :newEndTime " +
            "AND s.endTime >= :newStartTime")
    Optional<List<ShowTime>> findConflictingShows(@Param("theaterId") Long theaterId,
                                            @Param("newStartTime") OffsetDateTime newStartTime,
                                            @Param("newEndTime") OffsetDateTime newEndTime,
                                            @Param("showTimeIdToExclude") Long showTimeIdToExclude);



}
