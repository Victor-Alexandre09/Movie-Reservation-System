package Movie_Reservation_System_App.controller;

import Movie_Reservation_System_App.dto.reservedSeat.ReservedSeatResponseDto;
import Movie_Reservation_System_App.mapper.ReservedSeatMapper;
import Movie_Reservation_System_App.model.ReservedSeat;
import Movie_Reservation_System_App.service.ReservedSeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reserved-seats")
public class ReservedSeatController {

    private final ReservedSeatService reservedSeatService;
    private final ReservedSeatMapper reservedSeatMapper;

    public ReservedSeatController(ReservedSeatService reservedSeatService, ReservedSeatMapper reservedSeatMapper) {
        this.reservedSeatService = reservedSeatService;
        this.reservedSeatMapper = reservedSeatMapper;
    }

    @GetMapping("show-time/{showTimeId}")
    public ResponseEntity<List<ReservedSeatResponseDto>> getReservedSeatsByShowTimeId(@PathVariable Long showTimeId) {
        List<ReservedSeat> reservedSeatList = reservedSeatService.getReservedSeatIdsByShowTime(showTimeId);
        return ResponseEntity.ok(reservedSeatMapper.toDtoList(reservedSeatList));
    }
}
