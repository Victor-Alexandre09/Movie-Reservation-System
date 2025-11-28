package Movie_Reservation_System_App.controller;

import Movie_Reservation_System_App.dto.MovieDTO;
import Movie_Reservation_System_App.mapper.MovieMapper;
import Movie_Reservation_System_App.model.Movie;
import Movie_Reservation_System_App.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;
    private final MovieMapper movieMapper;

    public MovieController(MovieService movieService, MovieMapper movieMapper) {
        this.movieService = movieService;
        this.movieMapper = movieMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDTO.Response> createMovie(@RequestBody @Validated MovieDTO.Request data) {
        Movie createdMovie = movieService.createMovie(data);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdMovie.getId())
                .toUri();

        return ResponseEntity.created(location).body(movieMapper.toDTO(createdMovie));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO.Response> getMovie(@PathVariable Long id) {
        Movie movie = movieService.getMovie(id);
        return ResponseEntity.ok(movieMapper.toDTO(movie));
    }

    @GetMapping
    public ResponseEntity<List<MovieDTO.Response>> getMoviesList() {
        return ResponseEntity.ok(movieMapper.toDtoList(movieService.getMoviesList()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDTO.Response> updateMovie(@RequestBody @Validated MovieDTO.UpdateRequest data,
            @PathVariable Long id) {
        var movie = movieService.updateMovie(id, data);
        return ResponseEntity.ok(movieMapper.toDTO(movie));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
