package Movie_Reservation_System_App.controller;

import Movie_Reservation_System_App.dto.GenreDTO;
import Movie_Reservation_System_App.mapper.GenreMapper;
import Movie_Reservation_System_App.model.Genre;
import Movie_Reservation_System_App.service.GenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;
    private final GenreMapper genreMapper;

    public GenreController(GenreService genreService, GenreMapper genreMapper) {
        this.genreService = genreService;
        this.genreMapper = genreMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenreDTO.Response> createGenre(@RequestBody @Validated GenreDTO.Request data) {
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
    public ResponseEntity<GenreDTO.Response> getGenre(@PathVariable Long id) {
        Genre genre = genreService.getGenre(id);
        return ResponseEntity.ok(genreMapper.toDTO(genre));
    }

    @GetMapping
    public ResponseEntity<List<GenreDTO.Response>> getGenreList() {
        return ResponseEntity.ok(genreMapper.toDtoList(genreService.getGenresList()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenreDTO.Response> updateGenre(@RequestBody @Validated GenreDTO.Request data,
            @PathVariable Long id) {
        Genre newGenreData = genreMapper.toEntity(data);
        var genre = genreService.updateGenre(id, newGenreData);
        return ResponseEntity.ok(genreMapper.toDTO(genre));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}
