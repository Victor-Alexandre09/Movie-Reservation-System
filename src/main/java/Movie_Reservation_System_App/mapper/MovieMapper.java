package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.dto.movie.MovieRequestDto;
import Movie_Reservation_System_App.dto.movie.MovieResponseDto;
import Movie_Reservation_System_App.dto.movie.MovieUpdateRequestDto;
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

    MovieResponseDto toDTO(Movie movie);

    List<MovieResponseDto> toDtoList(List<Movie> movies);

    Movie toEntity(MovieRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMovieFromDto(MovieUpdateRequestDto dto, @MappingTarget Movie movie);
}
