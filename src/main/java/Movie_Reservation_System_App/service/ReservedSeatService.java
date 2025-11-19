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

    private final ReservedSeatRepository reservedSeatRepository;
    private final ShowTimeService showTimeService;

    public ReservedSeatService(ReservedSeatRepository reservedSeatRepository, ShowTimeService showTimeService) {
        this.reservedSeatRepository = reservedSeatRepository;
        this.showTimeService = showTimeService;
    }

    public ReservedSeat createReservedSeat(ReservedSeat reservedSeat) {
        Reservation reservation = reservedSeat.getReservation();
        ShowTime showTime = reservedSeat.getShowTime();
        Seat seat = reservedSeat.getSeat();

        validateSeatsAvailabilityWithLock(showTime.getId(), seat.getId());

        reservedSeat.setReservation(reservation);
        reservedSeat.setShowTime(showTime);
        reservedSeat.setSeat(seat);

        return reservedSeatRepository.save(reservedSeat);
    }

    public List<ReservedSeat> getReservedSeatsByShowTime(Long showTimeId) {
        showTimeService.getShowTime(showTimeId);
        return reservedSeatRepository.findAllByShowTimeId(showTimeId);
    }

    public void validateSeatsAvailabilityWithLock(Long showTimeId, Long seatId) {
        if (reservedSeatRepository.existsByShowTimeIdAndSeatId(showTimeId, seatId)) {
            throw new DuplicatedRegisterException("seat: " + seatId + " is already reserved");
        }
    }

    public void validateSeatsAvailabilityWithLock(Long showTimeId, List<Seat> seats) {
        for (Seat seat : seats) {
            Long seatId = seat.getId();

            if (reservedSeatRepository.existsByShowTimeIdAndSeatIdWithLock(showTimeId, seatId)) {
                throw new DuplicatedRegisterException("Seat " + seatId + " is already reserved");
            }
        }
    }

    public void saveAll(List<ReservedSeat> reservedSeatsToSave) {
        reservedSeatRepository.saveAll(reservedSeatsToSave);
    }

    public List<ReservedSeat> getReservedSeatIdsByShowTime(Long showTimeId) {
        showTimeService.getShowTime(showTimeId);
        return reservedSeatRepository.findReservedSeatsByShowTimeId(showTimeId);
    }
}
