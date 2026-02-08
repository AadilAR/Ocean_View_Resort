package oceanviewresort.service;

import oceanviewresort.dao.ReservationDAO;
import oceanviewresort.dao.ReservationDAOImpl;
import oceanviewresort.dao.RoomDAO;
import oceanviewresort.dao.RoomDAOImpl;
import oceanviewresort.model.Reservation;
import oceanviewresort.model.Room;

public class ReservationService {

    private final ReservationDAO reservationDAO = new ReservationDAOImpl();
    private final RoomDAO roomDAO = new RoomDAOImpl();
    private final BillingService billingService = new BillingService();

    public void createReservation(Reservation reservation) {

        Room room = roomDAO.getRoomById(
                reservation.getRoom().getRoomId()
        );

        double total = billingService.calculateTotal(
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                room.getPricePerNight()
        );

        reservation.setTotalAmount(total);
        reservationDAO.addReservation(reservation);
    }
}
