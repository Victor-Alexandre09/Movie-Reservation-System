package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.dto.ReservationDTO;
import Movie_Reservation_System_App.model.Reservation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ReservedSeatMapper.class, TheaterMapper.class })
public interface ReservationMapper {

    ReservationDTO.Response toDTO(Reservation reservation);

    List<ReservationDTO.Response> toDtoList(List<Reservation> reservations);

    Reservation toEntity(ReservationDTO.Request dto);
}
