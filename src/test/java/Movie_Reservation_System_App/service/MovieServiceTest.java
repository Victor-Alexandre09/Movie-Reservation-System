package Movie_Reservation_System_App.service;

import Movie_Reservation_System_App.dto.MovieDTO;
import Movie_Reservation_System_App.exception.DuplicatedRegisterException;
import Movie_Reservation_System_App.mapper.MovieMapper;
import Movie_Reservation_System_App.model.Genre;
import Movie_Reservation_System_App.model.Movie;
import Movie_Reservation_System_App.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

        @Mock
        private MovieRepository movieRepository;

        @Mock
        private MovieMapper movieMapper;

        @Mock
        private GenreService genreService;

        private MovieService movieService;

        @BeforeEach
        void setUp() {
                movieService = new MovieService(movieRepository, movieMapper, genreService);
        }

        @Test
        void testCreateMovie_WhenMovieDoesNotExist_ShouldSaveAndReturnMovie() {
                // Arrange
                MovieDTO.Request movieDto = new MovieDTO.Request(
                                "Inception",
                                "url",
                                120,
                                LocalDate.of(2000, 1, 2),
                                Set.of(1L, 2L));

                Genre genre1 = new Genre();
                genre1.setId(1L);
                genre1.setName("Sci-Fi");

                Genre genre2 = new Genre();
                genre2.setId(2L);
                genre2.setName("Thriller");

                Set<Genre> genres = Set.of(genre1, genre2);

                Movie movieToSave = new Movie();
                movieToSave.setTitle("Inception");
                movieToSave.setPosterImageUrl("url");
                movieToSave.setDurationMinutes(120);
                movieToSave.setReleaseDate(LocalDate.of(2000, 1, 2));
                movieToSave.setGenres(genres);

                Movie savedMovie = new Movie();
                savedMovie.setId(1L);
                savedMovie.setTitle("Inception");
                savedMovie.setPosterImageUrl("url");
                savedMovie.setDurationMinutes(120);
                savedMovie.setReleaseDate(LocalDate.of(2000, 1, 2));
                savedMovie.setGenres(genres);

                when(movieRepository.findByTitle("Inception")).thenReturn(Optional.empty());
                when(genreService.foundGenresById(movieDto.genreIds())).thenReturn(genres);
                when(movieMapper.toEntity(movieDto)).thenReturn(movieToSave);
                when(movieRepository.save(movieToSave)).thenReturn(savedMovie);

                Movie createdMovie = movieService.createMovie(movieDto);

                assertNotNull(createdMovie);
                assertEquals(1L, createdMovie.getId());
                assertEquals("Inception", createdMovie.getTitle());
                assertEquals(genres, createdMovie.getGenres());

                verify(movieRepository).findByTitle("Inception");
                verify(genreService).foundGenresById(movieDto.genreIds());
                verify(movieMapper).toEntity(movieDto);
                verify(movieRepository).save(movieToSave);
        }

        @Test
        void testCreateMovie_WhenMovieAlreadyExists_ShouldThrowDuplicatedRegisterException() {
                MovieDTO.Request movieDto = new MovieDTO.Request(
                                "Inception", "url", 120, LocalDate.now(), Set.of(1L, 2L));

                Movie existingMovie = new Movie(
                                1L, "Inception", "url", 120, LocalDate.now(), Set.of());

                when(movieRepository.findByTitle("Inception")).thenReturn(Optional.of(existingMovie));

                DuplicatedRegisterException exception = assertThrows(
                                DuplicatedRegisterException.class,
                                () -> movieService.createMovie(movieDto));

                assertEquals("the movie Inception already exists", exception.getMessage());
                verify(movieRepository).findByTitle("Inception");
                verify(genreService, never()).foundGenresById(any());
                verify(movieMapper, never()).toEntity(any());
                verify(movieRepository, never()).save(any(Movie.class));
        }

        @Test
        void testGetMovie_WhenMovieExists_ShouldReturnMovie() {
                Movie existingMovie = new Movie(
                                1L, "Inception", "url", 120, LocalDate.now(), Set.of());

                when(movieRepository.findById(1L)).thenReturn(Optional.of(existingMovie));

                Movie foundMovie = movieService.getMovie(1L);

                assertNotNull(foundMovie);
                assertEquals(1L, foundMovie.getId());
                verify(movieRepository).findById(1L);
        }

        @Test
        void testGetMovie_WhenMovieDoesNotExist_ShouldThrowEntityNotFoundException() {
                Long movieId = 99L;
                when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

                EntityNotFoundException exception = assertThrows(
                                EntityNotFoundException.class,
                                () -> movieService.getMovie(movieId));

                assertEquals("movie not found for id: " + movieId, exception.getMessage());
                verify(movieRepository).findById(movieId);
        }

        @Test
        void testGetMoviesList_WhenMoviesExist_ShouldReturnListOfMovies() {
                Movie movie1 = new Movie(
                                1L, "Movie 1", "url", 120, LocalDate.now(), Set.of());

                Movie movie2 = new Movie(
                                2L, "Movie 2", "url", 120, LocalDate.now(), Set.of());

                List<Movie> movieList = List.of(movie1, movie2);

                when(movieRepository.findAll()).thenReturn(movieList);

                List<Movie> result = movieService.getMoviesList();

                assertNotNull(result);
                assertEquals(2, result.size());
                assertEquals("Movie 1", result.get(0).getTitle());
                assertEquals("Movie 2", result.get(1).getTitle());
                verify(movieRepository).findAll();
        }

        @Test
        void testGetMoviesList_WhenNoMoviesExist_ShouldReturnEmptyList() {
                when(movieRepository.findAll()).thenReturn(Collections.emptyList());

                List<Movie> result = movieService.getMoviesList();

                assertNotNull(result);
                assertTrue(result.isEmpty());
                verify(movieRepository).findAll();
        }

        @Test
        void testUpdateMovie_WhenMovieExists_ShouldUpdateAndReturnMovie() {
                Long movieId = 1L;
                MovieDTO.UpdateRequest updateDto = new MovieDTO.UpdateRequest(
                                "Inception Updated", null, null, null, null);

                Movie existingMovie = new Movie(
                                1L, "Inception", "url", 120, LocalDate.now(), Set.of());

                when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
                doNothing().when(movieMapper).updateMovieFromDto(updateDto, existingMovie);
                when(movieRepository.save(existingMovie)).thenReturn(existingMovie);

                Movie updatedMovie = movieService.updateMovie(movieId, updateDto);

                assertNotNull(updatedMovie);
                verify(movieRepository).findById(movieId);
                verify(movieMapper).updateMovieFromDto(updateDto, existingMovie);
                verify(movieRepository).save(existingMovie);
        }

        @Test
        void testUpdateMovie_WhenMovieDoesNotExist_ShouldThrowEntityNotFoundException() {
                Long movieId = 99L;
                MovieDTO.UpdateRequest updateDto = new MovieDTO.UpdateRequest(
                                "Inception Updated", null, null, null, null);

                when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

                EntityNotFoundException exception = assertThrows(
                                EntityNotFoundException.class,
                                () -> movieService.updateMovie(movieId, updateDto));

                assertEquals("movie not found for id: 99", exception.getMessage());
                verify(movieRepository).findById(movieId);
                verify(movieMapper, never()).updateMovieFromDto(any(), any());
                verify(movieRepository, never()).save(any(Movie.class));
        }

        @Test
        void testDeleteMovie_WhenMovieExists_ShouldDeleteMovie() {
                Long movieId = 1L;
                Movie existingMovie = new Movie(
                                1L, "Inception", "url", 120, LocalDate.now(), Set.of());

                when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
                doNothing().when(movieRepository).delete(existingMovie);

                movieService.deleteMovie(movieId);

                verify(movieRepository).findById(movieId);
                verify(movieRepository).delete(existingMovie);
        }

        @Test
        void testDeleteMovie_WhenMovieDoesNotExist_ShouldThrowEntityNotFoundException() {
                Long movieId = 99L;

                when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

                EntityNotFoundException exception = assertThrows(
                                EntityNotFoundException.class,
                                () -> movieService.deleteMovie(movieId));

                assertEquals("movie not found for id: " + movieId, exception.getMessage());
                verify(movieRepository).findById(movieId);
                verify(movieRepository, never()).delete(any(Movie.class));
        }
}