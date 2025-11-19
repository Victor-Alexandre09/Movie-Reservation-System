package Movie_Reservation_System_App.controller;

import Movie_Reservation_System_App.dto.reservation.ReservationRequestDto;
import Movie_Reservation_System_App.dto.reservation.ReservationResponseDto;
import Movie_Reservation_System_App.mapper.ReservationMapper;
import Movie_Reservation_System_App.model.Reservation;
import Movie_Reservation_System_App.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    public ReservationController(ReservationService reservationService, ReservationMapper reservationMapper) {
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservationResponseDto> createShowTime(
            @RequestBody @Validated ReservationRequestDto reservationRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();

        Reservation reservation = reservationService.createReservation(reservationRequestDto, email);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservation.getId())
                .toUri();

        return ResponseEntity.created(location).body(reservationMapper.toDTO(reservation));
    }
}
