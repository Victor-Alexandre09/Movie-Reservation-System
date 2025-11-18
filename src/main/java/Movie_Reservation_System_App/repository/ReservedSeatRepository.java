package Movie_Reservation_System_App.repository;

import Movie_Reservation_System_App.model.ReservedSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, Long> {

    boolean existsByShowTimeIdAndSeatId(Long showTimeId, Long seatId);

    List<ReservedSeat> findAllByShowTimeId(Long showTimeId);
}
