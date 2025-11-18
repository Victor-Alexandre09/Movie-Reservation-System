package Movie_Reservation_System_App.exception;

public class ShowTimeAlreadyStartedException extends RuntimeException {
    public ShowTimeAlreadyStartedException(String message) {
        super(message);
    }
}
