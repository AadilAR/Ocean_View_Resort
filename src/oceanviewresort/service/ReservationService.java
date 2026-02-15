package oceanviewresort.service;

import oceanviewresort.dao.ReservationDAO;
import oceanviewresort.dao.ReservationDAOImpl;
import oceanviewresort.model.Reservation;

import java.time.LocalDate;
import java.util.List;

public class ReservationService {

    private final ReservationDAO reservationDAO = new ReservationDAOImpl();
    private final BillingService billingService = new BillingService();

    // ----------------------------------------
    // CREATE RESERVATION
    // ----------------------------------------

    public int createReservation(Reservation reservation) {

        LocalDate checkIn = reservation.getCheckInDate();
        LocalDate checkOut = reservation.getCheckOutDate();

        // Validate dates
        if (checkOut.isBefore(checkIn) || checkOut.equals(checkIn)) {
            throw new IllegalArgumentException("Invalid date range");
        }

        // Check room availability
        boolean available = reservationDAO.isRoomAvailable(
                reservation.getRoom().getRoomId(),
                checkIn,
                checkOut
        );

        if (!available) {
            return -1; // room not available
        }

        // Calculate total
        long days = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        double totalAmount = days * reservation.getRoom().getPricePerNight();
        reservation.setTotalAmount(totalAmount);

        // Save and return generated reservation ID
        return reservationDAO.save(reservation);
    }


    // ----------------------------------------
    // GET RESERVATION BY ID
    // ----------------------------------------

    public Reservation getReservationById(int reservationId) {
        return reservationDAO.findById(reservationId);
    }

    // ----------------------------------------
    // GET ALL RESERVATIONS
    // ----------------------------------------

    public List<Reservation> getAllReservations() {
        return reservationDAO.findAll();
    }

    // ----------------------------------------
    // SEARCH BY MOBILE NUMBER
    // ----------------------------------------

    public List<Reservation> searchByMobile(String mobile) {

        if (mobile == null || mobile.trim().isEmpty()) {
            throw new IllegalArgumentException("Mobile number required");
        }

        return reservationDAO.findByContactNumber(mobile.trim());
    }

    public boolean cancelReservation(int reservationId) {

        if (reservationId <= 0) {
            throw new IllegalArgumentException("Invalid reservation ID");
        }

        return reservationDAO.deleteById(reservationId);
    }
}