package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.dto.reservedSeat.ReservedSeatRequestDto;
import Movie_Reservation_System_App.dto.reservedSeat.ReservedSeatResponseDto;
import Movie_Reservation_System_App.model.ReservedSeat;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservedSeatMapper {

    ReservedSeatMapper INSTANCE = Mappers.getMapper(ReservedSeatMapper.class);

    ReservedSeatResponseDto toDTO(ReservedSeat reservedSeat);

    List<ReservedSeatResponseDto> toDtoList(List<ReservedSeat> reservedSeats);

    ReservedSeat toEntity(ReservedSeatRequestDto dto);
}
