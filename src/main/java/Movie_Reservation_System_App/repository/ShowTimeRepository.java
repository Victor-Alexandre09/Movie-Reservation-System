package Movie_Reservation_System_App.repository;

import Movie_Reservation_System_App.model.ShowTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {

    @Query("""
            SELECT s FROM ShowTime s WHERE s.theater.id = :theaterId
            AND s.id != :showTimeIdToExclude
            AND s.startTime <= :newEndTime
            AND s.endTime >= :newStartTime
            """)
    List<ShowTime> findConflictingShows(@Param("theaterId") Long theaterId,
                                        @Param("newStartTime") OffsetDateTime newStartTime,
                                        @Param("newEndTime") OffsetDateTime newEndTime,
                                        @Param("showTimeIdToExclude") Long showTimeIdToExclude);

    @EntityGraph(attributePaths = {"movie", "movie.genres",  "theater"})
    @Query("""
            SELECT s
            FROM ShowTime s
            WHERE s.movie.id = :movieId
            AND s.startTime > CURRENT_TIMESTAMP
            ORDER BY s.startTime ASC
            """)
    Page<ShowTime> findFutureShowsByMovieId(@Param("movieId") Long movieId, Pageable pageble);

    @EntityGraph(attributePaths = {"movie", "movie.genres",  "theater"})
    @Query("""
            SELECT s
            FROM ShowTime s
            WHERE s.startTime >= :startOfDay
            AND s.startTime < :endOfDay
            ORDER BY s.startTime ASC
            """)
    Page<ShowTime> findShowTimesByDate(@Param("startOfDay") OffsetDateTime startOfDay,
                                       @Param("endOfDay") OffsetDateTime endOfDay,
                                       Pageable pageable);
}
