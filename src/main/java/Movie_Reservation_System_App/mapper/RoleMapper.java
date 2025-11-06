package Movie_Reservation_System_App.mapper;

import Movie_Reservation_System_App.dto.role.RoleRequestDto;
import Movie_Reservation_System_App.dto.role.RoleResponseDto;
import Movie_Reservation_System_App.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleResponseDto toDTO(Role role);

    List<RoleResponseDto> toDtoList(List<Role> roles);

    Role toEntity(RoleRequestDto dto);
}
