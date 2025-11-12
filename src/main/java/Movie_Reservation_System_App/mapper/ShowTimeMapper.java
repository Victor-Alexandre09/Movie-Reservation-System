package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.dto.showTime.ShowTimeRequestDto;
import Movie_Reservation_System_App.dto.showTime.ShowTimeResponseDto;
import Movie_Reservation_System_App.dto.showTime.ShowTimeUpdateRequestDto;
import Movie_Reservation_System_App.model.ShowTime;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShowTimeMapper {

    ShowTimeMapper INSTANCE = Mappers.getMapper(ShowTimeMapper.class);

    ShowTimeResponseDto toDTO(ShowTime showTime);

    List<ShowTimeResponseDto> toDtoList(List<ShowTime> showTimes);

    ShowTime toEntity(ShowTimeRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateShowTimeFromDto(ShowTimeUpdateRequestDto dto, @MappingTarget ShowTime showTime);
}
