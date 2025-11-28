package Movie_Reservation_System_App.service;

import Movie_Reservation_System_App.dto.MovieDTO;
import Movie_Reservation_System_App.exception.DuplicatedRegisterException;
import Movie_Reservation_System_App.mapper.MovieMapper;
import Movie_Reservation_System_App.model.Genre;
import Movie_Reservation_System_App.model.Movie;
import Movie_Reservation_System_App.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final GenreService genreService;

    public MovieService(MovieRepository movieRepository, MovieMapper movieMapper, GenreService genreService) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
        this.genreService = genreService;
    }

    @Transactional
    public Movie createMovie(MovieDTO.Request movieDto) {
        if (movieRepository.findByTitle(movieDto.title()).isPresent()) {
            throw new DuplicatedRegisterException("the movie " + movieDto.title() + " already exists");
        }
        Set<Genre> genresList = genreService.foundGenresById(movieDto.genreIds());
        Movie movie = movieMapper.toEntity(movieDto);
        movie.setGenres(genresList);
        return movieRepository.save(movie);
    }

    public Movie getMovie(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("movie not found for id: " + id));
    }

    public List<Movie> getMoviesList() {
        return movieRepository.findAll();
    }

    public Movie updateMovie(Long id, MovieDTO.UpdateRequest updateDto) {
        Movie existingMovie = getMovie(id);
        movieMapper.updateMovieFromDto(updateDto, existingMovie);
        Set<Genre> newGenres = genreService.foundGenresById(updateDto.genreIds());
        existingMovie.setGenres(newGenres);
        movieRepository.save(existingMovie);
        return existingMovie;
    }

    public void deleteMovie(Long id) {
        Movie movie = getMovie(id);
        movieRepository.delete(movie);
    }
}
