package Movie_Reservation_System_App.controller;

import Movie_Reservation_System_App.dto.theater.TheaterRequestDto;
import Movie_Reservation_System_App.dto.theater.TheaterResponseDto;
import Movie_Reservation_System_App.dto.theater.TheaterUpdateRequestDto;
import Movie_Reservation_System_App.mapper.TheaterMapper;
import Movie_Reservation_System_App.model.Theater;
import Movie_Reservation_System_App.service.TheaterService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/theaters")
public class TheaterController {

    TheaterService theaterService;
    TheaterMapper theaterMapper;

    public TheaterController(TheaterService theaterService, TheaterMapper theaterMapper) {
        this.theaterService = theaterService;
        this.theaterMapper = theaterMapper;
    }

    @PostMapping
    public ResponseEntity<TheaterResponseDto> createTheater(@RequestBody @Validated TheaterRequestDto data) {
        Theater theater = theaterMapper.toEntity(data);
        Theater createdTheater = theaterService.createTheater(theater);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTheater.getId())
                .toUri();

        return ResponseEntity.created(location).body(theaterMapper.toDTO(createdTheater));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheaterResponseDto> getTheater(@PathVariable Long id) {
        Theater theater = theaterService.getTheater(id);
        return ResponseEntity.ok(theaterMapper.toDTO(theater));
    }

    @GetMapping
    public ResponseEntity<List<TheaterResponseDto>> getTheaterList() {
        return ResponseEntity.ok(theaterMapper.toDtoList(theaterService.getTheatersList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TheaterResponseDto> updateTheater(@RequestBody TheaterUpdateRequestDto data,
                                                            @PathVariable Long id) {
        Theater theater = theaterService.updateTheater(id, data);
        return ResponseEntity.ok(theaterMapper.toDTO(theater));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.noContent().build();
    }
}
