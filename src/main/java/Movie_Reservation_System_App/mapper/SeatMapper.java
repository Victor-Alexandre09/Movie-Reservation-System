package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.dto.SeatDTO;
import Movie_Reservation_System_App.model.Seat;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    SeatDTO.Response toDTO(Seat seat);

    List<SeatDTO.Response> toDtoList(List<Seat> seats);

    Seat toEntity(SeatDTO.Request dto);
}
