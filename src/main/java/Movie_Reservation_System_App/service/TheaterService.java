package Movie_Reservation_System_App.service;

import Movie_Reservation_System_App.controller.dto.theater.TheaterUpdateRequestDto;
import Movie_Reservation_System_App.exception.DuplicatedRegisterException;
import Movie_Reservation_System_App.mapper.TheaterMapper;
import Movie_Reservation_System_App.model.Theater;
import Movie_Reservation_System_App.repository.TheaterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheaterService {

    TheaterRepository theaterRepository;
    TheaterMapper theaterMapper;

    public TheaterService(TheaterRepository theaterRepository, TheaterMapper theaterMapper) {
        this.theaterRepository = theaterRepository;
        this.theaterMapper = theaterMapper;
    }

    public Theater createTheater(Theater theater) {
        if (theaterRepository.findByName(theater.getName()).isPresent()) {
            throw new DuplicatedRegisterException("the theater " + theater.getName() + " already exists");
        }
        return theaterRepository.save(theater);
    }

    public Theater getTheater(Long id) {
        return theaterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("theater not found for id: " + id));
    }

    public List<Theater> getTheatersList() {
        return theaterRepository.findAll();
    }

    public Theater updateTheater(Long id, TheaterUpdateRequestDto updateDto) {
        Theater existingTheader = getTheater(id);
        theaterMapper.updateTheaterFromDto(updateDto, existingTheader);
        return theaterRepository.save(existingTheader);
    }

    public void deleteTheater(Long id) {
        Theater theater = getTheater(id);
        theaterRepository.delete(theater);
    }
}
