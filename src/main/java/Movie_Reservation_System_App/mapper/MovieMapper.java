package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.dto.MovieDTO;
import Movie_Reservation_System_App.model.Movie;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    MovieDTO.Response toDTO(Movie movie);

    List<MovieDTO.Response> toDtoList(List<Movie> movies);

    Movie toEntity(MovieDTO.Request dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMovieFromDto(MovieDTO.UpdateRequest dto, @MappingTarget Movie movie);
}
