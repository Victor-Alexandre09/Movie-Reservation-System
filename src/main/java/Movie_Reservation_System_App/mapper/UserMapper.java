package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.dto.theater.TheaterUpdateRequestDto;
import Movie_Reservation_System_App.dto.user.UserRequestDto;
import Movie_Reservation_System_App.dto.user.UserResponseDto;
import Movie_Reservation_System_App.model.Theater;
import Movie_Reservation_System_App.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponseDto toDTO(User user);

    List<UserResponseDto> toDtoList(List<User> users);

    User toEntity(UserRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTheaterFromDto(TheaterUpdateRequestDto dto, @MappingTarget Theater theater);
}
