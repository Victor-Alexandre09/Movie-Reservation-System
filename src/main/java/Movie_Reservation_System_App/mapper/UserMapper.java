package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.dto.UserDTO;
import Movie_Reservation_System_App.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO.Response toDTO(User user);

    List<UserDTO.Response> toDtoList(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserDTO.Request dto);
}
