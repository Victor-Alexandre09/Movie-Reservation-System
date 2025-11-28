package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.dto.ShowTimeDTO;
import Movie_Reservation_System_App.model.ShowTime;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShowTimeMapper {

    ShowTimeMapper INSTANCE = Mappers.getMapper(ShowTimeMapper.class);

    ShowTimeDTO.Response toDTO(ShowTime showTime);

    List<ShowTimeDTO.Response> toDtoList(List<ShowTime> showTimes);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "theater", ignore = true)
    ShowTime toEntity(ShowTimeDTO.Request dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "theater", ignore = true)
    void updateShowTimeFromDto(ShowTimeDTO.UpdateRequest dto, @MappingTarget ShowTime showTime);
}
