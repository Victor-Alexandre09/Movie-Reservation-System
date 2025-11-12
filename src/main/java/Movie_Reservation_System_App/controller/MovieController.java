package Movie_Reservation_System_App.controller;

import Movie_Reservation_System_App.dto.movie.MovieRequestDto;
import Movie_Reservation_System_App.dto.movie.MovieResponseDto;
import Movie_Reservation_System_App.dto.movie.MovieUpdateRequestDto;
import Movie_Reservation_System_App.mapper.MovieMapper;
import Movie_Reservation_System_App.model.Movie;
import Movie_Reservation_System_App.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    public MovieService movieService;
    public MovieMapper movieMapper;

    public MovieController(MovieService movieService, MovieMapper movieMapper) {
        this.movieService = movieService;
        this.movieMapper = movieMapper;
    }

    @PostMapping
    public ResponseEntity<MovieResponseDto> createMovie(@RequestBody @Validated MovieRequestDto movieRequestDto) {
        Movie createdMovie = movieService.createMovie(movieRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdMovie.getId())
                .toUri();

        return ResponseEntity.created(location).body(movieMapper.toDTO(createdMovie));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDto> getMovie(@PathVariable Long id) {
        Movie movie = movieService.getMovie(id);
        return ResponseEntity.ok(movieMapper.toDTO(movie));
    }

    @GetMapping
    public ResponseEntity<List<MovieResponseDto>> getMovieList() {
        return ResponseEntity.ok(movieMapper.toDtoList(movieService.getMoviesList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponseDto> updateMovie(@RequestBody MovieUpdateRequestDto data,
                                                            @PathVariable Long id) {
        Movie updatedMovie = movieService.updateMovie(id, data);
        return ResponseEntity.ok(movieMapper.toDTO(updatedMovie));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
