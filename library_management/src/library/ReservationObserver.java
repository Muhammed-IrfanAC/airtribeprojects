package library;

public interface ReservationObserver {
    void notifyReservationAvailable(String patronId, String isbn);
}

