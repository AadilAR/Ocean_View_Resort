package oceanviewresort.controller;

import oceanviewresort.model.Reservation;
import oceanviewresort.model.Room;
import oceanviewresort.service.ReservationService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/reserve")
public class ReservationServlet extends HttpServlet {

    private static final String RESERVATION_PAGE = "/reservation.html";
    private static final String LOGIN_PAGE = "/login.html";

    private final ReservationService reservationService = new ReservationService();

    // =====================================================
    // POST → CREATE RESERVATION
    // =====================================================

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        if (!isLoggedIn(request)) {
            redirect(response, request, LOGIN_PAGE);
            return;
        }

        try {
            Reservation reservation = buildReservationFromRequest(request);

            boolean success = reservationService.createReservation(reservation);

            if (success) {
                redirect(response, request,
                        RESERVATION_PAGE + "?success=booked");
            } else {
                redirect(response, request,
                        RESERVATION_PAGE + "?error=unavailable");
            }

        } catch (IllegalArgumentException e) {
            redirect(response, request,
                    RESERVATION_PAGE + "?error=invalidDate");

        } catch (Exception e) {
            e.printStackTrace();
            redirect(response, request,
                    RESERVATION_PAGE + "?error=server");
        }
    }

    // =====================================================
    // GET → SEARCH BY MOBILE (NO JSP)
    // =====================================================

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        if (!isLoggedIn(request)) {
            redirect(response, request, LOGIN_PAGE);
            return;
        }

        String searchMobile = request.getParameter("search");

        // If no search parameter → just go back to page
        if (searchMobile == null || searchMobile.trim().isEmpty()) {
            redirect(response, request, RESERVATION_PAGE);
            return;
        }

        List<Reservation> results =
                reservationService.searchByMobile(searchMobile.trim());

        response.setContentType("text/html");
        var out = response.getWriter();

        out.println("<html><head><title>Search Results</title></head><body>");
        out.println("<h2>Search Results for Mobile: " + searchMobile + "</h2>");

        if (results.isEmpty()) {
            out.println("<p>No reservations found.</p>");
        } else {
            out.println("<table border='1' cellpadding='8'>");
            out.println("<tr><th>ID</th><th>Guest</th><th>Room</th><th>Check-In</th><th>Check-Out</th><th>Total</th></tr>");

            for (Reservation r : results) {
                out.println("<tr>");
                out.println("<td>" + r.getReservationId() + "</td>");
                out.println("<td>" + r.getGuestName() + "</td>");
                out.println("<td>" + r.getRoom().getRoomId() + "</td>");
                out.println("<td>" + r.getCheckInDate() + "</td>");
                out.println("<td>" + r.getCheckOutDate() + "</td>");
                out.println("<td>" + r.getTotalAmount() + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
        }

        out.println("<br><a href='" + request.getContextPath()
                + "/reservation.html'>Back to Reservation Page</a>");
        out.println("</body></html>");
    }

    // =====================================================
    // HELPER METHODS
    // =====================================================

    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("loggedUser") != null;
    }

    private Reservation buildReservationFromRequest(HttpServletRequest request) {

        String guestName = request.getParameter("guestName");
        String address = request.getParameter("address");
        String contactNumber = request.getParameter("contactNumber");

        int roomId = Integer.parseInt(request.getParameter("roomId"));

        LocalDate checkIn = LocalDate.parse(request.getParameter("checkIn"));
        LocalDate checkOut = LocalDate.parse(request.getParameter("checkOut"));

        Room room = new Room();
        room.setRoomId(roomId);

        // Your edited code (kept)
        room.setPricePerNight(5000);

        Reservation reservation = new Reservation();
        reservation.setGuestName(guestName);
        reservation.setAddress(address);
        reservation.setContactNumber(contactNumber);
        reservation.setRoom(room);
        reservation.setCheckInDate(checkIn);
        reservation.setCheckOutDate(checkOut);

        return reservation;
    }

    private void redirect(HttpServletResponse response,
                          HttpServletRequest request,
                          String path) throws IOException {

        response.sendRedirect(request.getContextPath() + path);
    }
}