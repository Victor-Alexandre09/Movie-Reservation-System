package Movie_Reservation_System_App.controller;

import Movie_Reservation_System_App.dto.seat.SeatRequestDto;
import Movie_Reservation_System_App.dto.seat.SeatResponseDto;
import Movie_Reservation_System_App.service.SeatService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SeatResponseDto> createSeat(@RequestBody @Valid SeatRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seatService.createSeat(dto));
    }

    @PostMapping("/row-of-seats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SeatResponseDto>> createRowOfSeats(@RequestBody @Valid SeatRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seatService.createRowOfSeats(dto));
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<SeatResponseDto>> getSeatsByTheater(@PathVariable Long theaterId) {
        return ResponseEntity.ok(seatService.getSeatsByTheater(theaterId));
    }

    @DeleteMapping("/{seatId}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long seatId) {
        seatService.deleteSeat(seatId);
        return ResponseEntity.noContent().build();
    }
}
