package Movie_Reservation_System_App.repository;

import Movie_Reservation_System_App.model.ReservedSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, Long> {

    boolean existsByShowTimeIdAndSeatId(Long showTimeId, Long seatId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT CASE WHEN COUNT(rs) > 0
            THEN true ELSE false END
            FROM ReservedSeat rs
            JOIN rs.reservation r
            WHERE rs.showTime.id = :showTimeId
            AND rs.seat.id = :seatId
            AND r.status != 'CANCELLED'
            """)
    boolean existsByShowTimeIdAndSeatIdWithLock(
            @Param("showTimeId") Long showTimeId,
            @Param("seatId") Long seatId);

    List<ReservedSeat> findAllByShowTimeId(Long showTimeId);

    @Query("""
            SELECT rs FROM ReservedSeat rs
            JOIN FETCH rs.seat s
            WHERE rs.showTime.id = :showTimeId
            AND rs.reservation.status != 'CANCELLED'
            """)
    List<ReservedSeat> findReservedSeatsByShowTimeId(Long showTimeId);
}
