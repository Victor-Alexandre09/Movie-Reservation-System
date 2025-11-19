package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.dto.reservedSeat.ReservedSeatRequestDto;
import Movie_Reservation_System_App.dto.reservedSeat.ReservedSeatResponseDto;
import Movie_Reservation_System_App.model.ReservedSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservedSeatMapper {

    ReservedSeatMapper INSTANCE = Mappers.getMapper(ReservedSeatMapper.class);

    @Mapping(source = "seat.id", target = "seatId")
    @Mapping(source = "seat.row", target = "row")
    @Mapping(source = "seat.number", target = "number")
    ReservedSeatResponseDto toDTO(ReservedSeat reservedSeat);

    List<ReservedSeatResponseDto> toDtoList(List<ReservedSeat> reservedSeats);

    ReservedSeat toEntity(ReservedSeatRequestDto dto);
}
