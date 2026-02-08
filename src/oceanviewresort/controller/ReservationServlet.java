package oceanviewresort.controller;

import oceanviewresort.model.Reservation;
import oceanviewresort.model.Room;
import oceanviewresort.service.ReservationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/reserve")
public class ReservationServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Reservation reservation = new Reservation();
        reservation.setGuestName(request.getParameter("guestName"));
        reservation.setAddress(request.getParameter("address"));
        reservation.setContactNumber(request.getParameter("contactNumber"));

        Room room = new Room();
        room.setRoomId(Integer.parseInt(request.getParameter("roomId")));
        reservation.setRoom(room);

        reservation.setCheckInDate(
                LocalDate.parse(request.getParameter("checkIn"))
        );
        reservation.setCheckOutDate(
                LocalDate.parse(request.getParameter("checkOut"))
        );

        reservationService.createReservation(reservation);

        response.getWriter().println("Reservation created successfully");
    }
}
