package Movie_Reservation_System_App.service;

import Movie_Reservation_System_App.dto.seat.SeatRequestDto;
import Movie_Reservation_System_App.dto.seat.SeatResponseDto;
import Movie_Reservation_System_App.exception.DuplicatedRegisterException;
import Movie_Reservation_System_App.model.Seat;
import Movie_Reservation_System_App.model.Theater;
import Movie_Reservation_System_App.repository.SeatRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
    public SeatResponseDto createSeat(SeatRequestDto dto) {
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

        return new SeatResponseDto(seat.getId(), theater.getId(), seat.getRow(), seat.getNumber());
    }

    public List<SeatResponseDto> getSeatsByTheater(Long theaterId) {
        return seatRepository.findAllByTheaterId(theaterId).stream()
                .map(s -> new SeatResponseDto(s.getId(), s.getTheater().getId(), s.getRow(), s.getNumber()))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<SeatResponseDto> createRowOfSeats(@Valid SeatRequestDto dto) {
        List<SeatResponseDto> seatsListResponse = new ArrayList<>();

        Long theaterId = dto.theaterId();
        String seatRow = dto.row().toUpperCase();
        int numberOfSeats = dto.number();

        for (int i = 0; i < numberOfSeats; i++) {
            int seatNumber = i + 1;
            SeatRequestDto seatRequestDto = new SeatRequestDto(theaterId, seatRow, seatNumber);
            seatsListResponse.add(createSeat(seatRequestDto));
        }

        return seatsListResponse;
    }

    @Transactional
    public void deleteSeat(Long seatId) {
        seatRepository.findById(seatId).orElseThrow(
                () -> new EntityNotFoundException("seat not found for id " + seatId)
        );
        seatRepository.deleteById(seatId);
    }
}