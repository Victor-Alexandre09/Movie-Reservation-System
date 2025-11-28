package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.dto.RoleDTO;
import Movie_Reservation_System_App.model.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDTO.Response toDTO(Role role);

    List<RoleDTO.Response> toDtoList(List<Role> roles);

    Role toEntity(RoleDTO.Request dto);
}
