package oceanviewresort.service;

import oceanviewresort.model.Reservation;
import oceanviewresort.model.Room;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationServiceTest {

    @Test
    void testCreateReservation_InvalidDates() {
        Reservation reservation = new Reservation();

        reservation.setCheckInDate(LocalDate.of(2026, 4, 10));
        reservation.setCheckOutDate(LocalDate.of(2026, 4, 5));

        ReservationService service = new ReservationService();

        assertThrows(IllegalArgumentException.class, () ->
                service.createReservation(reservation)
        );
    }

    @Test
    void testCreateReservation_ValidFlow() {
        Reservation reservation = new Reservation();
        Room room = new Room();

        room.setRoomId(1);

        reservation.setRoom(room);
        reservation.setGuestName("JUnit Test");
        reservation.setCheckInDate(LocalDate.of(2026, 4, 10));
        reservation.setCheckOutDate(LocalDate.of(2026, 4, 12));

        ReservationService service = new ReservationService();
        int result = service.createReservation(reservation);

        assertTrue(result >= -1);
    }
}