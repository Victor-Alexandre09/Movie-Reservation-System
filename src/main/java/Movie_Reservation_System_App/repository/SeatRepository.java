package Movie_Reservation_System_App.repository;

import Movie_Reservation_System_App.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findAllByTheaterId(Long theaterId);

    boolean existsByTheaterIdAndRowAndNumber(Long theaterId, String row, Integer number);

    List<Seat> findAllByIdInAndTheaterId(Collection<Long> seatId, Long theaterId);
}