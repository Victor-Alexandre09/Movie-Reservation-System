package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.dto.TheaterDTO;
import Movie_Reservation_System_App.model.Theater;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TheaterMapper {

    TheaterDTO.Response toDTO(Theater theater);

    List<TheaterDTO.Response> toDtoList(List<Theater> theaters);

    @Mapping(target = "id", ignore = true)
    Theater toEntity(TheaterDTO.Request dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateTheaterFromDto(TheaterDTO.UpdateRequest dto, @MappingTarget Theater theater);
}
