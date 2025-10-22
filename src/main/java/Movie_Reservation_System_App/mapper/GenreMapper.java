package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.controller.dto.genre.GenreRequestDto;
import Movie_Reservation_System_App.controller.dto.genre.GenreResponseDto;
import Movie_Reservation_System_App.model.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    GenreResponseDto toDTO(Genre genre);

    List<GenreResponseDto> toDtoList(List<Genre> genres);

    Genre toEntity(GenreRequestDto dto);
}
