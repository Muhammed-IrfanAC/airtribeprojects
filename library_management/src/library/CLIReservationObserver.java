package library;

public class CLIReservationObserver implements ReservationObserver {
    @Override
    public void notifyReservationAvailable(String patronId, String isbn) {
        System.out.println("Notification: library.Patron " + patronId + " can now check out book " + isbn + ".");
    }
}

