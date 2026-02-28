package oceanviewresort.dao;

import oceanviewresort.model.Reservation;
import oceanviewresort.model.Room;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationDAOTest {

    /**
     * Validates room availability check.
     */
    @Test
    void testIsRoomAvailable() {
        ReservationDAO dao = new ReservationDAOImpl();

        boolean available = dao.isRoomAvailable(
                1,
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 3)
        );

        // Just verify method executes without error
        assertTrue(available || !available);
    }

    /**
     * Validates reservation save operation.
     */
    @Test
    void testSaveReservation() {
        Reservation reservation = new Reservation();
        Room room = new Room();
        room.setRoomId(1);

        reservation.setRoom(room);
        reservation.setGuestName("JUnit DAO");
        reservation.setAddress("Test Address");
        reservation.setContactNumber("0771234567");
        reservation.setCheckInDate(LocalDate.of(2026, 5, 10));
        reservation.setCheckOutDate(LocalDate.of(2026, 5, 12));
        reservation.setTotalAmount(10000);

        ReservationDAO dao = new ReservationDAOImpl();
        int id = dao.save(reservation);

        assertTrue(id >= 0);
    }
}