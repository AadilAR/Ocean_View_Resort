package oceanviewresort.dao;

import oceanviewresort.model.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {

    /**
     * Saves a reservation.
     * @return true if saved successfully
     */
    int save(Reservation reservation);

    /**
     * Finds reservation by ID.
     */
    Reservation findById(int reservationId);

    /**
     * Returns all reservations.
     */
    List<Reservation> findAll();

    List<Reservation> findByContactNumber(String contactNumber);

    boolean deleteById(int reservationId);

    /**
     * Checks if a room is available for a given date range.
     */
    boolean isRoomAvailable(int roomId,
                            LocalDate checkIn,
                            LocalDate checkOut);
}