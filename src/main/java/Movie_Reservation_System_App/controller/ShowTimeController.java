package Movie_Reservation_System_App.controller;

import Movie_Reservation_System_App.dto.showTime.ShowTimeRequestDto;
import Movie_Reservation_System_App.dto.showTime.ShowTimeResponseDto;
import Movie_Reservation_System_App.dto.showTime.ShowTimeUpdateRequestDto;
import Movie_Reservation_System_App.mapper.ShowTimeMapper;
import Movie_Reservation_System_App.model.ShowTime;
import Movie_Reservation_System_App.service.ShowTimeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.OffsetDateTime;

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
    @PreAuthorize("hasRole('ADMIN')")
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
    public ResponseEntity<Page<ShowTimeResponseDto>> getShowTimeList(
            @PageableDefault(size = 10, sort = "startTime") Pageable pageable) {

        Page<ShowTime> showTimePage = showTimeService.getShowTimeList(pageable);
        return ResponseEntity.ok(showTimePage.map(showTimeMapper::toDTO));
    }

    @GetMapping("/by-movie/{movieId}")
    public ResponseEntity<Page<ShowTimeResponseDto>> getShowTimesByMovie(
            @PathVariable Long movieId,
            @PageableDefault(size = 10, sort = "startTime") Pageable pageable) {

        Page<ShowTime> showTimes = showTimeService.getShowTimesByMovieId(movieId, pageable);
        return ResponseEntity.ok(showTimes.map(showTimeMapper::toDTO));
    }

    @GetMapping("/by-date")
    public ResponseEntity<Page<ShowTimeResponseDto>> getShowTimesByDate(
            @RequestParam OffsetDateTime date,
            @PageableDefault(size = 10, sort = "startTime") Pageable pageable) {

        Page<ShowTime> showTimes = showTimeService.getShowTimesByDate(date, pageable);
        return ResponseEntity.ok(showTimes.map(showTimeMapper::toDTO));
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
