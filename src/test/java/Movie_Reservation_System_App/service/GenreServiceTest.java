package Movie_Reservation_System_App.service;

import Movie_Reservation_System_App.exception.DuplicatedRegisterException;
import Movie_Reservation_System_App.model.Genre;
import Movie_Reservation_System_App.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreService genreService;

    @BeforeEach
    void setUp() {
        // Inicializa o serviço com o repositório mockado antes de cada teste
        genreService = new GenreService(genreRepository);
    }

    @Test
    void testCreateGenre_WhenGenreDoesNotExist_ShouldSaveAndReturnGenre() {
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Action");

        when(genreRepository.findByName("Action")).thenReturn(Optional.empty());
        when(genreRepository.save(genre)).thenReturn(genre);

        Genre cratedGenre = genreService.createGenre(genre);

        assertNotNull(cratedGenre);
        assertEquals(1L, cratedGenre.getId());
        assertEquals("Action", cratedGenre.getName());
        verify(genreRepository).findByName("Action");
        verify(genreRepository).save(genre);
    }

    @Test
    void testCreateGenre_WhenGenreAlreadyExists_ShouldThrowDuplicatedRegisterException() {
        Genre existingGenre = new Genre();
        existingGenre.setId(1L);
        existingGenre.setName("Action");

        Genre genreToCreate = new Genre();
        genreToCreate.setName("Action");

        when(genreRepository.findByName("Action")).thenReturn(Optional.of(existingGenre));

        DuplicatedRegisterException exception = assertThrows(
                DuplicatedRegisterException.class,
                () -> genreService.createGenre(genreToCreate)
        );

        assertEquals("the genre Action already exists", exception.getMessage());
        verify(genreRepository, never()).save(any(Genre.class));
    }

    @Test
    void testGetGenre_WhenGenreExists_ShouldReturnGenre() {
        Genre existingGenre = new Genre();
        existingGenre.setId(1L);
        existingGenre.setName("Comedy");

        when(genreRepository.findById(1L)).thenReturn(Optional.of(existingGenre));

        Genre cratedGenre = genreService.getGenre(1L);

        assertNotNull(cratedGenre);
        assertEquals(1L, cratedGenre.getId());
        verify(genreRepository).findById(1L);
    }

    @Test
    void testGetGenre_WhenGenreDoesNotExist_ShouldThrowEntityNotFoundException() {
        Long genreId = 99L;

        when(genreRepository.findById(genreId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> genreService.getGenre(genreId)
        );

        assertEquals("genre not found for id: " + genreId, exception.getMessage());
        verify(genreRepository).findById(genreId);
    }

    @Test
    void testGetGenresList_WhenGenresExist_ShouldReturnListOfGenres() {
        Genre genre1 = new Genre();
        genre1.setId(1L);
        genre1.setName("Sci-Fi");

        Genre genre2 = new Genre();
        genre2.setId(2L);
        genre2.setName("Drama");

        List<Genre> genreList = List.of(genre1, genre2);

        when(genreRepository.findAll()).thenReturn(genreList);

        List<Genre> result = genreService.getGenresList();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Sci-Fi", result.get(0).getName());
        assertEquals("Drama", result.get(1).getName());
        verify(genreRepository).findAll();
    }

    @Test
    void testGetGenresList_WhenNoGenresExist_ShouldReturnEmptyList() {
        when(genreRepository.findAll()).thenReturn(Collections.emptyList());

        List<Genre> result = genreService.getGenresList();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(genreRepository).findAll();
    }

    @Test
    void testUpdateGenre_WhenGenreExists_ShouldUpdateAndReturnGenre() {
        Long genreId = 1L;

        Genre existingGenre = new Genre();
        existingGenre.setId(genreId);
        existingGenre.setName("Old Name");

        Genre newGenreData = new Genre();
        newGenreData.setName("New Name");

        when(genreRepository.findById(genreId)).thenReturn(Optional.of(existingGenre));
        when(genreRepository.save(existingGenre)).thenReturn(existingGenre);

        Genre updatedGenre = genreService.updateGenre(genreId, newGenreData);

        assertNotNull(updatedGenre);
        assertEquals("New Name", updatedGenre.getName());
        verify(genreRepository).findById(genreId);
        verify(genreRepository).save(existingGenre);
    }

    @Test
    void testUpdateGenre_WhenGenreDoesNotExist_ShouldThrowEntityNotFoundException() {
        Long genreId = 99L;
        Genre newGenreData = new Genre();
        newGenreData.setName("New Name");

        when(genreRepository.findById(genreId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> genreService.updateGenre(genreId, newGenreData)
        );

        assertEquals("genre not found for id: 99", exception.getMessage());
        verify(genreRepository).findById(genreId);
        verify(genreRepository, never()).save(any(Genre.class));
    }

    @Test
    void testDeleteGenre_WhenGenreExists_ShouldDeleteGenre() {
        Long genreId = 1L;
        Genre existingGenre = new Genre();
        existingGenre.setId(genreId);
        existingGenre.setName("To Be Deleted");

        when(genreRepository.findById(genreId)).thenReturn(Optional.of(existingGenre));
        doNothing().when(genreRepository).delete(existingGenre);

        genreService.deleteGenre(genreId);

        verify(genreRepository).findById(genreId);
        verify(genreRepository).delete(existingGenre);
    }

    @Test
    void testDeleteGenre_WhenGenreDoesNotExist_ShouldThrowEntityNotFoundException() {
        Long genreId = 99L;

        when(genreRepository.findById(genreId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> genreService.deleteGenre(genreId)
        );

        assertEquals("genre not found for id: " + genreId, exception.getMessage());
        verify(genreRepository).findById(genreId);
        verify(genreRepository, never()).delete(any(Genre.class));
    }

    @Test
    void testFoundGenresById_WhenAllIdsExist_ShouldReturnSetOfGenres() {
        Set<Long> idSet = Set.of(1L, 2L);

        Genre genre1 = new Genre();
        genre1.setId(1L);
        Genre genre2 = new Genre();
        genre2.setId(2L);
        Set<Genre> genresFound = Set.of(genre1, genre2);

        when(genreRepository.findAllByIdIn(idSet)).thenReturn(genresFound);

        Set<Genre> result = genreService.foundGenresById(idSet);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(genre1));
        assertTrue(result.contains(genre2));
        verify(genreRepository).findAllByIdIn(idSet);
    }

    @Test
    void testFoundGenresById_WhenSomeIdsDoNotExist_ShouldThrowEntityNotFoundException() {
        Set<Long> idSet = Set.of(1L, 2L, 99L);

        Genre genre1 = new Genre();
        genre1.setId(1L);
        Genre genre2 = new Genre();
        genre2.setId(2L);

        Set<Genre> genresFound = Set.of(genre1, genre2);

        when(genreRepository.findAllByIdIn(idSet)).thenReturn(genresFound);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> genreService.foundGenresById(idSet)
        );

        assertEquals("One or more of genre IDs were not found", exception.getMessage());
        verify(genreRepository).findAllByIdIn(idSet);
    }

    @Test
    void testFoundGenresById_WhenIdSetIsEmpty_ShouldReturnEmptySet() {
        Set<Long> idSet = Collections.emptySet();

        Set<Genre> result = genreService.foundGenresById(idSet);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(genreRepository);
    }

    @Test
    void testFoundGenresById_WhenIdSetIsNull_ShouldReturnEmptySet() {
        Set<Long> idSet = null;

        Set<Genre> result = genreService.foundGenresById(idSet);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(genreRepository);
    }
}