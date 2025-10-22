package Movie_Reservation_System_App.repository;

import Movie_Reservation_System_App.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByName(String name);

    boolean existsByName(String name);

    Set<Genre> findAllByIdIn(Set<Long> idSet);
}
