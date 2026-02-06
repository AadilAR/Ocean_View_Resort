package oceanviewresort.dao;

import oceanviewresort.model.Reservation;

import java.util.List;

public interface ReservationDAO {

    void addReservation(Reservation reservation);

    Reservation getReservationById(int reservationId);

    List<Reservation> getAllReservations();
}
