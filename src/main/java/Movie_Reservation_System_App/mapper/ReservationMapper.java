package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.dto.reservation.ReservationRequestDto;
import Movie_Reservation_System_App.dto.reservation.ReservationResponseDto;
import Movie_Reservation_System_App.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ReservedSeatMapper.class})
public interface ReservationMapper {

    Reservation INSTANCE = Mappers.getMapper(Reservation.class);

    ReservationResponseDto toDTO(Reservation reservation);

    List<ReservationResponseDto> toDtoList(List<Reservation> reservations);

    Reservation toEntity(ReservationRequestDto dto);
}
