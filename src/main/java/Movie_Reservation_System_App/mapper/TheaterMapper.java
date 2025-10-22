package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.controller.dto.theater.TheaterRequestDto;
import Movie_Reservation_System_App.controller.dto.theater.TheaterResponseDto;
import Movie_Reservation_System_App.controller.dto.theater.TheaterUpdateRequestDto;
import Movie_Reservation_System_App.model.Theater;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TheaterMapper {

    TheaterMapper INSTANCE = Mappers.getMapper(TheaterMapper.class);

    TheaterResponseDto toDTO(Theater theater);

    List<TheaterResponseDto> toDtoList(List<Theater> theaters);

    Theater toEntity(TheaterRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTheaterFromDto(TheaterUpdateRequestDto dto, @MappingTarget Theater theater);
}
