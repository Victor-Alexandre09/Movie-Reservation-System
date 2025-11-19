package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.dto.seat.SeatRequestDto;
import Movie_Reservation_System_App.dto.seat.SeatResponseDto;
import Movie_Reservation_System_App.model.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    SeatMapper INSTANCE = Mappers.getMapper(SeatMapper.class);

    SeatResponseDto toDTO(Seat seat);

    List<SeatResponseDto> toDtoList(List<Seat> seats);

    Seat toEntity(SeatRequestDto dto);
}
