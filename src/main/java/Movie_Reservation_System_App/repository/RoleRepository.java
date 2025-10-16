package Movie_Reservation_System_App.repository;

import Movie_Reservation_System_App.model.Genre;
import Movie_Reservation_System_App.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByName(String name);

    boolean existsByName(String name);
}
