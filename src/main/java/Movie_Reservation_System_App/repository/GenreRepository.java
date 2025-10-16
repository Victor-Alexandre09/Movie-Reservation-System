package Movie_Reservation_System_App.repository;

import Movie_Reservation_System_App.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
