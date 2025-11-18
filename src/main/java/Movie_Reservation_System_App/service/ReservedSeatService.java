package Movie_Reservation_System_App.service;

import Movie_Reservation_System_App.exception.DuplicatedRegisterException;
import Movie_Reservation_System_App.model.Reservation;
import Movie_Reservation_System_App.model.ReservedSeat;
import Movie_Reservation_System_App.model.Seat;
import Movie_Reservation_System_App.model.ShowTime;
import Movie_Reservation_System_App.repository.ReservedSeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservedSeatService {

    ReservedSeatRepository reservedSeatRepository;
    ShowTimeService showTimeService;
    TheaterService theaterService;
    SeatService seatService;

    public ReservedSeatService(ReservedSeatRepository reservedSeatRepository, ShowTimeService showTimeService, TheaterService theaterService, SeatService seatService) {
        this.reservedSeatRepository = reservedSeatRepository;
        this.showTimeService = showTimeService;
        this.theaterService = theaterService;
        this.seatService = seatService;
    }

    public ReservedSeat createReservedSeat(ReservedSeat reservedSeat) {
        Reservation reservation = reservedSeat.getReservation();
        ShowTime showTime = reservedSeat.getShowTime();
        Seat seat = reservedSeat.getSeat();

        validateSeatsAvailability(showTime.getId(), seat.getId());

        reservedSeat.setReservation(reservation);
        reservedSeat.setShowTime(showTime);
        reservedSeat.setSeat(seat);

        return reservedSeatRepository.save(reservedSeat);
    }

    public List<ReservedSeat> getReservedSeatsByShowTime(Long showTimeId) {
        showTimeService.getShowTime(showTimeId);
        return reservedSeatRepository.findAllByShowTimeId(showTimeId);
    }

    public void validateSeatsAvailability(Long showTimeId, Long seatId) {
        if (reservedSeatRepository.existsByShowTimeIdAndSeatId(showTimeId, seatId)) {
            throw new DuplicatedRegisterException("seat: " + seatId + " is already reserved");
        }
    }

    public void saveAll(List<ReservedSeat> reservedSeatsToSave) {
        reservedSeatRepository.saveAll(reservedSeatsToSave);
    }
}
