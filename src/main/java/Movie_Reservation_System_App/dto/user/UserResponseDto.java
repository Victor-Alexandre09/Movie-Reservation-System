package Movie_Reservation_System_App.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserResponseDto (
        Long id,
        String name,
        String email
){
}
