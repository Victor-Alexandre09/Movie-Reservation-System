package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.controller.dto.movie.MovieDto;
import Movie_Reservation_System_App.model.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    MovieDto toDTO(Movie movie);

    Movie toEntity(MovieDto dto);
}
