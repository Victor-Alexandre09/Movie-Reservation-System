package Movie_Reservation_System_App.service;

import Movie_Reservation_System_App.dto.ReservationDTO;
import Movie_Reservation_System_App.exception.ExceededMaxNumberOfSeatsReservation;
import Movie_Reservation_System_App.exception.ShowTimeAlreadyStartedException;
import Movie_Reservation_System_App.mapper.ReservationMapper;
import Movie_Reservation_System_App.model.*;
import Movie_Reservation_System_App.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final ShowTimeService showTimeService;
    private final ReservedSeatService reservedSeatService;
    private final SeatService seatService;
    private final UserService userService;

    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper,
            ShowTimeService showTimeService, ReservedSeatService reservedSeatService, SeatService seatService,
            UserService userService) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.showTimeService = showTimeService;
        this.reservedSeatService = reservedSeatService;
        this.seatService = seatService;
        this.userService = userService;
    }

    @Transactional
    public Reservation createReservation(ReservationDTO.Request dto, String userEmail) {

        User user = userService.getUserByEmail(userEmail);
        ShowTime showTime = showTimeService.getShowTimeWithTheater(dto.showTimeId());
        List<Seat> seatList = seatService.foundSeatListByIdInTheater(dto.seatId(), showTime.getTheater().getId());

        businessValidation(showTime, seatList);

        reservedSeatService.validateSeatsAvailabilityWithLock(showTime.getId(), seatList);

        BigDecimal totalPrice = showTime.getPrice().multiply(BigDecimal.valueOf(seatList.size()));

        Reservation reservation = new Reservation();
        reservation.setShowTime(showTime);
        reservation.setTotalPrice(totalPrice);
        reservation.setUser(user);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);

        List<ReservedSeat> reservedSeatsToSave = seatList.stream()
                .map(seat -> {
                    ReservedSeat reservedSeat = new ReservedSeat();

                    reservedSeat.setSeat(seat);
                    reservedSeat.setReservation(reservation);
                    reservedSeat.setShowTime(showTime);

                    return reservedSeat;
                }).toList();

        reservedSeatService.saveAll(reservedSeatsToSave);

        reservation.setReservedSeats(reservedSeatsToSave);

        return reservation;
    }

    private void businessValidation(ShowTime showTime, List<Seat> seatList) {
        if (showTime.getStartTime().isBefore(OffsetDateTime.now())) {
            throw new ShowTimeAlreadyStartedException(
                    "It is not possible to realize the reservation, the show time already started");
        }
        if (seatList.size() > 8) {
            throw new ExceededMaxNumberOfSeatsReservation("Can not reserve more than 8 seats");

        }
    }
}
