package Movie_Reservation_System_App.controller;

import Movie_Reservation_System_App.dto.reservation.ReservationRequestDto;
import Movie_Reservation_System_App.dto.reservation.ReservationResponseDto;
import Movie_Reservation_System_App.dto.reservedSeat.ReservedSeatRequestDto;
import Movie_Reservation_System_App.mapper.ReservedSeatMapper;
import Movie_Reservation_System_App.model.Reservation;
import Movie_Reservation_System_App.model.ReservedSeat;
import Movie_Reservation_System_App.service.ReservedSeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class ReservedSeatController {
//
//    ReservedSeatService reservedSeatService;
//    ReservedSeatMapper reservedSeatMapper;
//
//    public ReservedSeatController(ReservedSeatService reservedSeatService, ReservedSeatMapper reservedSeatMapper) {
//        this.reservedSeatService = reservedSeatService;
//        this.reservedSeatMapper = reservedSeatMapper;
//    }
//
//    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<ReservationResponseDto> createShowTime(@RequestBody @Validated ReservedSeatRequestDto requestDto) {
//        ReservedSeat reservedSeat = reservedSeatService.createReservedSeat()
//
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(reservation.getId())
//                .toUri();
//
//        return ResponseEntity.created(location).body(reservationMapper.toDTO(reservation));
//    }
}
