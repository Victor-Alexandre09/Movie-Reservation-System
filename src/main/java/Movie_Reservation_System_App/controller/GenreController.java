package Movie_Reservation_System_App.controller;

import Movie_Reservation_System_App.dto.genre.GenreRequestDto;
import Movie_Reservation_System_App.dto.genre.GenreResponseDto;
import Movie_Reservation_System_App.mapper.GenreMapper;
import Movie_Reservation_System_App.model.Genre;
import Movie_Reservation_System_App.service.GenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    GenreService genreService;
    GenreMapper genreMapper;

    public GenreController(GenreService genreService, GenreMapper genreMapper) {
        this.genreService = genreService;
        this.genreMapper = genreMapper;
    }

    @PostMapping
    public ResponseEntity<GenreResponseDto> createGenre(@RequestBody @Validated GenreRequestDto data) {
        Genre genre = genreMapper.toEntity(data);
        Genre createdGenre = genreService.createGenre(genre);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdGenre.getId())
                .toUri();

        return ResponseEntity.created(location).body(genreMapper.toDTO(createdGenre));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponseDto> getGenre(@PathVariable Long id) {
        Genre genre = genreService.getGenre(id);
        return ResponseEntity.ok(genreMapper.toDTO(genre));
    }

    @GetMapping
    public ResponseEntity<List<GenreResponseDto>> getGenreList() {
        return ResponseEntity.ok(genreMapper.toDtoList(genreService.getGenresList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreResponseDto> updateGenre(@RequestBody @Validated GenreRequestDto data,
                                                        @PathVariable Long id) {
        Genre newGenreData = genreMapper.toEntity(data);
        var genre = genreService.updateGenre(id, newGenreData);
        return ResponseEntity.ok(genreMapper.toDTO(genre));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}
