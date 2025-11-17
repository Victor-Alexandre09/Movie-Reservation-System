package Movie_Reservation_System_App.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "poster_image_url")
    private String posterImageUrl;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "movies_genres",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "genres_id")
    )
    @BatchSize(size = 100)
    private Set<Genre> genres = new HashSet<>();
}