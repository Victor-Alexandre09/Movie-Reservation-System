package Movie_Reservation_System_App.exception;

public class ExceededMaxNumberOfSeatsReservation extends RuntimeException {
    public ExceededMaxNumberOfSeatsReservation(String message) {
        super(message);
    }
}
