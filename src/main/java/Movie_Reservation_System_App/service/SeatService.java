package Movie_Reservation_System_App.service;

import Movie_Reservation_System_App.dto.SeatDTO;
import Movie_Reservation_System_App.exception.DuplicatedRegisterException;
import Movie_Reservation_System_App.model.Seat;
import Movie_Reservation_System_App.model.Theater;
import Movie_Reservation_System_App.repository.SeatRepository;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final TheaterService theaterService;

    public SeatService(SeatRepository seatRepository, TheaterService theaterService) {
        this.seatRepository = seatRepository;
        this.theaterService = theaterService;
    }

    @Transactional
    public SeatDTO.Response createSeat(SeatDTO.Request dto) {
        Theater theater = theaterService.getTheater(dto.theaterId());
        String upperCaseRow = dto.row().toUpperCase();

        if (seatRepository.existsByTheaterIdAndRowAndNumber(theater.getId(), upperCaseRow, dto.number())) {
            throw new DuplicatedRegisterException(
                    "Seat " + dto.row() + "-" + dto.number() + " already exists in theater " + theater.getId());
        }

        Seat seat = new Seat();
        seat.setRow(upperCaseRow);
        System.out.println(dto.row().toUpperCase());
        seat.setNumber(dto.number());
        seat.setTheater(theater);

        seatRepository.save(seat);

        return new SeatDTO.Response(seat.getId(), theater.getId(), seat.getRow(), seat.getNumber());
    }

    public List<SeatDTO.Response> getSeatsByTheater(Long theaterId) {
        return seatRepository.findAllByTheaterId(theaterId).stream()
                .map(s -> new SeatDTO.Response(s.getId(), s.getTheater().getId(), s.getRow(), s.getNumber()))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<SeatDTO.Response> createRowOfSeats(@Valid SeatDTO.Request dto) {
        List<SeatDTO.Response> seatsListResponse = new ArrayList<>();

        Long theaterId = dto.theaterId();
        String seatRow = dto.row().toUpperCase();
        int numberOfSeats = dto.number();

        for (int i = 0; i < numberOfSeats; i++) {
            int seatNumber = i + 1;
            SeatDTO.Request seatRequestDto = new SeatDTO.Request(theaterId, seatRow, seatNumber);
            seatsListResponse.add(createSeat(seatRequestDto));
        }

        return seatsListResponse;
    }

    @Transactional
    public void deleteSeat(Long seatId) {
        seatRepository.findById(seatId).orElseThrow(
                () -> new EntityNotFoundException("seat not found for id " + seatId));
        seatRepository.deleteById(seatId);
    }

    public List<Seat> foundSeatListByIdInTheater(List<Long> idList, Long theaterId) {
        if (idList == null || idList.isEmpty()) {
            return List.of();
        }

        Set<Long> uniqueIds = new HashSet<>(idList);

        if (idList.size() > uniqueIds.size()) {
            throw new DuplicateRequestException("the same seat ID was requested multiple times");
        }

        List<Seat> seatsFound = seatRepository.findAllByIdInAndTheaterId(uniqueIds, theaterId);

        if (seatsFound.size() != uniqueIds.size()) {
            throw new EntityNotFoundException("One or more of seat IDs were not found");
        }
        return seatsFound;
    }
}