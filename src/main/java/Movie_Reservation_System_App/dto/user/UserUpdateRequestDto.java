package Movie_Reservation_System_App.dto.user;

public record UserUpdateRequestDto(
        String name,
        String password,
        String email
) {
}
