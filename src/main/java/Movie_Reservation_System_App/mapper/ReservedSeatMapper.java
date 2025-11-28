package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.dto.ReservedSeatDTO;
import Movie_Reservation_System_App.model.ReservedSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservedSeatMapper {

    @Mapping(source = "seat.id", target = "seatId")
    @Mapping(source = "seat.row", target = "row")
    @Mapping(source = "seat.number", target = "number")
    ReservedSeatDTO.Response toDTO(ReservedSeat reservedSeat);

    List<ReservedSeatDTO.Response> toDtoList(List<ReservedSeat> reservedSeats);

    ReservedSeat toEntity(ReservedSeatDTO.Request dto);
}
