package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.dto.GenreDTO;
import Movie_Reservation_System_App.model.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    GenreDTO.Response toDTO(Genre genre);

    List<GenreDTO.Response> toDtoList(List<Genre> genres);

    Genre toEntity(GenreDTO.Request dto);
}
