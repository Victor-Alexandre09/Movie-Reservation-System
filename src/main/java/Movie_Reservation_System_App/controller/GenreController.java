package Movie_Reservation_System_App.controller;

import Movie_Reservation_System_App.controller.dto.movie.MovieDto;
import Movie_Reservation_System_App.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController {

    MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@RequestBody @Validated MovieDto data) {
        System.out.println("____DATA____" + data);
        movieService.createMovie(data);
        return ResponseEntity.ok().build();
    }
}
