package oceanviewresort.dao;

import oceanviewresort.model.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReservationDAO {

    /**
     * Saves a reservation.
     * @return generated reservation ID
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

    /**
     * Finds reservations by contact number.
     */
    List<Reservation> findByContactNumber(String contactNumber);

    /**
     * Deletes reservation by ID.
     */
    boolean deleteById(int reservationId);

    /**
     * Checks if a room is available for a given date range.
     */
    boolean isRoomAvailable(int roomId,
                            LocalDate checkIn,
                            LocalDate checkOut);

    // ==========================================
    // REPORT METHODS (For Decision Making)
    // ==========================================

    /**
     * Returns total revenue grouped by month.
     */
    List<Map<String, Object>> getMonthlyRevenue();

    /**
     * Returns total bookings grouped by month.
     */
    List<Map<String, Object>> getMonthlyOccupancy();

    /**
     * Returns the most booked room.
     */
    Map<String, Object> getMostBookedRoom();
}