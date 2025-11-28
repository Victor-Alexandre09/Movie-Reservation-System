package Movie_Reservation_System_App.service;

import Movie_Reservation_System_App.dto.TheaterDTO;
import Movie_Reservation_System_App.mapper.TheaterMapper;
import Movie_Reservation_System_App.model.Theater;
import Movie_Reservation_System_App.repository.TheaterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final TheaterMapper theaterMapper;

    public TheaterService(TheaterRepository theaterRepository, TheaterMapper theaterMapper) {
        this.theaterRepository = theaterRepository;
        this.theaterMapper = theaterMapper;
    }

    public Theater createTheater(TheaterDTO.Request theaterRequestDto) {
        Theater theater = theaterMapper.toEntity(theaterRequestDto);
        return theaterRepository.save(theater);
    }

    public Theater getTheater(Long id) {
        return theaterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Theater not found for id: " + id));
    }

    public Page<Theater> getTheaterList(Pageable pageable) {
        return theaterRepository.findAll(pageable);
    }

    public Theater updateTheater(Long id, TheaterDTO.UpdateRequest theaterUpdateRequestDto) {
        Theater theater = getTheater(id);
        theaterMapper.updateTheaterFromDto(theaterUpdateRequestDto, theater);
        return theaterRepository.save(theater);
    }

    public void deleteTheater(Long id) {
        theaterRepository.delete(getTheater(id));
    }
}
