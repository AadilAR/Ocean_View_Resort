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
    // POST → CREATE OR CANCEL RESERVATION
    // =====================================================

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        if (!isLoggedIn(request)) {
            redirect(response, request, LOGIN_PAGE);
            return;
        }

        // -------- CANCEL RESERVATION --------
        String cancelId = request.getParameter("cancelId");

        if (cancelId != null && !cancelId.isBlank()) {
            handleCancel(cancelId, request, response);
            return;
        }

        // -------- CREATE RESERVATION --------
        try {
            Reservation reservation = buildReservationFromRequest(request);

            boolean success = reservationService.createReservation(reservation);

            if (success) {
                redirect(response, request, RESERVATION_PAGE + "?success=booked");
            } else {
                redirect(response, request, RESERVATION_PAGE + "?error=unavailable");
            }

        } catch (IllegalArgumentException e) {
            redirect(response, request, RESERVATION_PAGE + "?error=invalidDate");

        } catch (Exception e) {
            e.printStackTrace();
            redirect(response, request, RESERVATION_PAGE + "?error=server");
        }
    }

    // =====================================================
    // GET → SEARCH BY MOBILE
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

        if (searchMobile == null || searchMobile.trim().isEmpty()) {
            redirect(response, request, RESERVATION_PAGE);
            return;
        }

        List<Reservation> results =
                reservationService.searchByMobile(searchMobile.trim());

        renderSearchResults(response, request, searchMobile, results);
    }

    // =====================================================
    // HANDLE CANCEL LOGIC
    // =====================================================

    private void handleCancel(String cancelId,
                              HttpServletRequest request,
                              HttpServletResponse response)
            throws IOException {

        try {
            int reservationId = Integer.parseInt(cancelId);

            boolean deleted =
                    reservationService.cancelReservation(reservationId);

            if (deleted) {
                redirect(response, request,
                        RESERVATION_PAGE + "?success=cancelled");
            } else {
                redirect(response, request,
                        RESERVATION_PAGE + "?error=notfound");
            }

        } catch (Exception e) {
            redirect(response, request,
                    RESERVATION_PAGE + "?error=server");
        }
    }

    // =====================================================
    // RENDER SEARCH RESULTS
    // =====================================================

    private void renderSearchResults(HttpServletResponse response,
                                     HttpServletRequest request,
                                     String searchMobile,
                                     List<Reservation> results)
            throws IOException {

        response.setContentType("text/html");
        var out = response.getWriter();

        out.println("""
        <html>
        <head>
            <title>Search Results</title>
            <style>
                body { font-family: Segoe UI, Arial; background:#f4f7fa; margin:0; }
                .container {
                    width: 850px;
                    margin: 40px auto;
                    background: white;
                    padding: 30px;
                    border-radius: 10px;
                    box-shadow: 0 5px 20px rgba(0,0,0,0.1);
                }
                h2 { color:#2c5364; }
                table {
                    width:100%;
                    border-collapse: collapse;
                    margin-top:20px;
                }
                th, td {
                    padding:10px;
                    border:1px solid #ddd;
                    text-align:center;
                }
                th {
                    background:#2c5364;
                    color:white;
                }
                .cancel-btn {
                    background:#c0392b;
                    color:white;
                    border:none;
                    padding:6px 12px;
                    border-radius:4px;
                    cursor:pointer;
                }
                .cancel-btn:hover {
                    background:#922b21;
                }
                a {
                    text-decoration:none;
                    color:#2c5364;
                    font-weight:bold;
                }
            </style>
        </head>
        <body>
        <div class="container">
        """);

        out.println("<h2>Search Results for Mobile: " + searchMobile + "</h2>");

        if (results.isEmpty()) {
            out.println("<p>No reservations found.</p>");
        } else {
            out.println("<table>");
            out.println("<tr><th>ID</th><th>Guest</th><th>Room</th><th>Check-In</th><th>Check-Out</th><th>Total</th><th>Action</th></tr>");

            for (Reservation r : results) {
                out.println("<tr>");
                out.println("<td>" + r.getReservationId() + "</td>");
                out.println("<td>" + r.getGuestName() + "</td>");
                out.println("<td>" + r.getRoom().getRoomId() + "</td>");
                out.println("<td>" + r.getCheckInDate() + "</td>");
                out.println("<td>" + r.getCheckOutDate() + "</td>");
                out.println("<td>" + r.getTotalAmount() + "</td>");

                out.println("<td>");
                out.println("<form method='post' action='" + request.getContextPath() + "/reserve'>");
                out.println("<input type='hidden' name='cancelId' value='" + r.getReservationId() + "'>");
                out.println("<button type='submit' class='cancel-btn' " +
                        "onclick=\"return confirm('Are you sure you want to cancel this reservation?');\">" +
                        "Cancel</button>");
                out.println("</form>");
                out.println("</td>");

                out.println("</tr>");
            }

            out.println("</table>");
        }

        out.println("<br><a href='" + request.getContextPath() +
                "/reservation.html'>Back to Reservation Page</a>");
        out.println("</div></body></html>");
    }

    // =====================================================
    // HELPER METHODS
    // =====================================================

    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null &&
                session.getAttribute("loggedUser") != null;
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