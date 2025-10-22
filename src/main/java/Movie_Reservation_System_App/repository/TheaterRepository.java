package Movie_Reservation_System_App.repository;

import Movie_Reservation_System_App.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TheaterRepository extends JpaRepository<Theater, Long> {

    Optional<Theater> findByName(String name);

    boolean existsByName(String name);
}
