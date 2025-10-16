package Movie_Reservation_System_App.service;

import Movie_Reservation_System_App.controller.dto.genre.GenreRequestDto;
import Movie_Reservation_System_App.exception.DuplicatedRegisterException;
import Movie_Reservation_System_App.mapper.GenreMapper;
import Movie_Reservation_System_App.model.Genre;
import Movie_Reservation_System_App.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Genre createGenre(Genre genre) {
        if (genreRepository.findByName(genre.getName()).isPresent()) {
            throw new DuplicatedRegisterException("the genre" + genre.getName() + " already exists");
        }
        genreRepository.save(genre);
        return genre;
    }

    public Genre getGenre(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("genre not found for id: " + id));
    }

    public List<Genre> getGenresList() {
        return genreRepository.findAll();
    }

    public Genre updateGenre(Long id, Genre newGenreData) {
        Genre genre = getGenre(id);
        genre.setName(newGenreData.getName());
        genreRepository.save(genre);
        return genre;
    }

    public void deleteGenre(Long id) {
        Genre genre = getGenre(id);
        genreRepository.delete(genre);
    }
}
