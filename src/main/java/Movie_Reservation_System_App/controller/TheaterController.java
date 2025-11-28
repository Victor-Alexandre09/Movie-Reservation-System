package Movie_Reservation_System_App.controller;

import Movie_Reservation_System_App.dto.TheaterDTO;
import Movie_Reservation_System_App.mapper.TheaterMapper;
import Movie_Reservation_System_App.model.Theater;
import Movie_Reservation_System_App.service.TheaterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/theaters")
public class TheaterController {

    private final TheaterService theaterService;
    private final TheaterMapper theaterMapper;

    public TheaterController(TheaterService theaterService, TheaterMapper theaterMapper) {
        this.theaterService = theaterService;
        this.theaterMapper = theaterMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TheaterDTO.Response> createTheater(
            @RequestBody @Validated TheaterDTO.Request theaterRequestDto) {
        Theater createdTheater = theaterService.createTheater(theaterRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTheater.getId())
                .toUri();

        return ResponseEntity.created(location).body(theaterMapper.toDTO(createdTheater));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheaterDTO.Response> getTheater(@PathVariable Long id) {
        return ResponseEntity.ok(theaterMapper.toDTO(theaterService.getTheater(id)));
    }

    @GetMapping
    public ResponseEntity<Page<TheaterDTO.Response>> getTheaterList(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<Theater> theaterPage = theaterService.getTheaterList(pageable);
        return ResponseEntity.ok(theaterPage.map(theaterMapper::toDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TheaterDTO.Response> updateTheater(@PathVariable Long id,
            @RequestBody TheaterDTO.UpdateRequest theaterUpdateRequestDto) {
        Theater updatedTheater = theaterService.updateTheater(id, theaterUpdateRequestDto);
        return ResponseEntity.ok(theaterMapper.toDTO(updatedTheater));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.noContent().build();
    }
}
