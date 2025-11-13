package Movie_Reservation_System_App.controller;

import Movie_Reservation_System_App.dto.showTime.ShowTimeRequestDto;
import Movie_Reservation_System_App.dto.showTime.ShowTimeResponseDto;
import Movie_Reservation_System_App.dto.showTime.ShowTimeUpdateRequestDto;
import Movie_Reservation_System_App.mapper.ShowTimeMapper;
import Movie_Reservation_System_App.model.ShowTime;
import Movie_Reservation_System_App.service.ShowTimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/show-times")
public class ShowTimeController {

    ShowTimeService showTimeService;
    ShowTimeMapper showTimeMapper;

    public ShowTimeController(ShowTimeService showTimeService, ShowTimeMapper showTimeMapper) {
        this.showTimeService = showTimeService;
        this.showTimeMapper = showTimeMapper;
    }

    @PostMapping
    public ResponseEntity<ShowTimeResponseDto> createShowTime(@RequestBody @Validated ShowTimeRequestDto showTimeRequestDto) {
        ShowTime createdShowTime = showTimeService.createShowTime(showTimeRequestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdShowTime.getId())
                .toUri();

        return ResponseEntity.created(location).body(showTimeMapper.toDTO(createdShowTime));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowTimeResponseDto> getShowTime(@PathVariable Long id) {
        return ResponseEntity.ok(showTimeMapper.toDTO(showTimeService.getShowTime(id)));
    }

    @GetMapping
    public ResponseEntity<List<ShowTimeResponseDto>> getShowTimeList() {
        return ResponseEntity.ok(showTimeMapper.toDtoList(showTimeService.getShowTimeList()));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShowTimeResponseDto> updateShowTime(@PathVariable Long id,
                                                              @RequestBody ShowTimeUpdateRequestDto updateRequestDto) {
        ShowTime updatedShowTime = showTimeService.updateShowTime(id, updateRequestDto);
        return ResponseEntity.ok(showTimeMapper.toDTO(updatedShowTime));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteShowTime(@PathVariable Long id) {
        showTimeService.deleteShowTime(id);
        return ResponseEntity.noContent().build();
    }
}
