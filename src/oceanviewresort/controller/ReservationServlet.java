package oceanviewresort.controller;

import oceanviewresort.dao.RoomDAO;
import oceanviewresort.dao.RoomDAOImpl;
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

    private static final String LOGIN_PAGE = "/login.html";

    private final ReservationService reservationService = new ReservationService();
    private final RoomDAO roomDAO = new RoomDAOImpl();

    // =====================================================
    // POST → CREATE OR CANCEL
    // =====================================================

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        if (!isLoggedIn(request)) {
            redirect(response, request, LOGIN_PAGE);
            return;
        }

        String cancelId = request.getParameter("cancelId");

        if (cancelId != null && !cancelId.isBlank()) {
            handleCancel(cancelId, response, request);
            return;
        }

        handleCreate(request, response);
    }

    // =====================================================
    // GET → MAIN PAGE / SEARCH / VIEW ALL / VIEW ROOMS
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
        String viewAll = request.getParameter("viewAll");
        String viewRooms = request.getParameter("viewRooms");

        if (viewAll != null) {
            List<Reservation> results =
                    reservationService.getAllReservations();
            renderResults(response, request,
                    "All Reservations", results);
            return;
        }

        if (searchMobile != null && !searchMobile.trim().isEmpty()) {
            List<Reservation> results =
                    reservationService.searchByMobile(searchMobile.trim());
            renderResults(response, request,
                    "Search Results for Mobile: " + searchMobile.trim(),
                    results);
            return;
        }

        if (viewRooms != null) {
            renderAllRooms(response, request);
            return;
        }

        renderReservationPage(response, request);
    }

    // =====================================================
    // HANDLE CREATE
    // =====================================================

    private void handleCreate(HttpServletRequest request,
                              HttpServletResponse response)
            throws IOException {

        try {
            Reservation reservation = buildReservationFromRequest(request);

            int reservationId =
                    reservationService.createReservation(reservation);

            if (reservationId > 0) {

                response.sendRedirect(request.getContextPath()
                        + "/bill?reservationId=" + reservationId);

            } else {

                response.sendRedirect(request.getContextPath()
                        + "/reserve?error=unavailable");
            }


        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/reserve?error=invalidDate");

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/reserve?error=server");
        }
    }

    // =====================================================
    // HANDLE CANCEL
    // =====================================================

    private void handleCancel(String cancelId,
                              HttpServletResponse response,
                              HttpServletRequest request)
            throws IOException {

        try {
            int reservationId = Integer.parseInt(cancelId);

            boolean deleted =
                    reservationService.cancelReservation(reservationId);

            if (deleted) {
                response.sendRedirect(request.getContextPath() + "/reserve?success=cancelled");
            } else {
                response.sendRedirect(request.getContextPath() + "/reserve?error=notfound");
            }

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/reserve?error=server");
        }
    }

    // =====================================================
    // MAIN PAGE (FORM + ROOM TABLE)
    // =====================================================

    private void renderReservationPage(HttpServletResponse response,
                                       HttpServletRequest request)
            throws IOException {

        List<Room> rooms = roomDAO.findAll();
        List<Reservation> reservations =
                reservationService.getAllReservations();

        String success = request.getParameter("success");
        String error = request.getParameter("error");

        response.setContentType("text/html;charset=UTF-8");
        var out = response.getWriter();

        out.println("""
        <html>
        <head>
            <title>Reservation</title>
            <style>
                body { font-family: Segoe UI; background:#f4f7fa; margin:0; }
                .wrapper { display:flex; gap:40px; padding:40px; }
                .box {
                    background:white;
                    padding:30px;
                    border-radius:10px;
                    box-shadow:0 5px 20px rgba(0,0,0,0.1);
                }
                .form-box { width:420px; }
                .room-box { flex:1; }
                h2 { color:#2c5364; }
                input {
                    width:100%;
                    padding:8px;
                    margin:10px 0;
                }
                button {
                    padding:10px;
                    background:#2c5364;
                    color:white;
                    border:none;
                    cursor:pointer;
                    margin-bottom:10px;
                }
                table {
                    width:100%;
                    border-collapse:collapse;
                }
                th, td {
                    border:1px solid #ddd;
                    padding:8px;
                    text-align:center;
                }
                th { background:#2c5364; color:white; }
                .success { color:green; margin-top:15px; }
                .error { color:red; margin-top:10px; }
            </style>
        </head>
        <body>
        <div class='wrapper'>
        """);

        // -------- LEFT FORM --------
        out.println("<div class='box form-box'>");
        out.println("<h2>Book Room</h2>");

        if ("unavailable".equals(error))
            out.println("<div class='error'>Room unavailable for selected dates.</div>");
        if ("invalidDate".equals(error))
            out.println("<div class='error'>Invalid date range.</div>");
        if ("cancelled".equals(success))
            out.println("<div class='success'>Reservation cancelled!</div>");

        //out.println("<a href='" + request.getContextPath() + "/reserve?viewAll=true'><button>View All Reservations</button></a>");
        //out.println("<a href='" + request.getContextPath() + "/reserve?viewRooms=true'><button>View All Rooms</button></a>");

        out.println("<form method='post' action='" + request.getContextPath() + "/reserve'>");
        out.println("<input type='text' name='guestName' placeholder='Guest Name' required>");
        out.println("<input type='text' name='address' placeholder='Address'>");
        out.println("<input type='text' name='contactNumber' placeholder='Contact Number' required>");
        out.println("<input type='number' name='roomId' placeholder='Room ID' required>");
        out.println("<input type='date' name='checkIn' required>");
        out.println("<input type='date' name='checkOut' required>");
        out.println("<button type='submit'>Reserve</button>");

        // ✅ MOVED SUCCESS MESSAGE TO BOTTOM + PRINT BUTTON
        if ("booked".equals(success)) {
            out.println("<div class='success'>Reservation successful!</div>");
            out.println("<button onclick=\"window.print()\">Print Bill</button>");
        }

        out.println("</form>");
        out.println("</div>");

        // -------- RIGHT ROOM TABLE --------
        out.println("<div class='box room-box'>");
        out.println("<h2>Rooms & Bookings</h2>");
        out.println("<table>");
        out.println("<tr><th>ID</th><th>Type</th><th>Price</th><th>Booked Dates</th></tr>");

        for (Room room : rooms) {

            out.println("<tr>");
            out.println("<td>" + room.getRoomId() + "</td>");
            out.println("<td>" + room.getRoomType() + "</td>");
            out.println("<td>" + room.getPricePerNight() + "</td>");

            StringBuilder booked = new StringBuilder();

            for (Reservation r : reservations) {
                if (r.getRoom().getRoomId() == room.getRoomId()) {
                    booked.append(r.getCheckInDate())
                            .append(" to ")
                            .append(r.getCheckOutDate())
                            .append("<br>");
                }
            }

            out.println("<td>" +
                    (booked.length() == 0 ? "Available" : booked.toString())
                    + "</td>");

            out.println("</tr>");
        }

        out.println("</table>");
        out.println("</div>");
        out.println("</div></body></html>");
    }
    private void renderAllRooms(HttpServletResponse response,
                                HttpServletRequest request)
            throws IOException {

        List<Room> rooms = roomDAO.findAll();

        response.setContentType("text/html;charset=UTF-8");

        var out = response.getWriter();

        out.println("""
    <html>
    <head>
        <title>All Rooms</title>
        <style>
            body {
                font-family: Segoe UI, Arial;
                background:#f4f7fa;
                margin:0;
                padding:40px;
            }
            .container {
                background:white;
                padding:30px;
                border-radius:10px;
                box-shadow:0 5px 20px rgba(0,0,0,0.1);
                max-width:900px;
                margin:auto;
            }
            h2 {
                color:#2c5364;
                margin-bottom:20px;
            }
            table {
                width:100%;
                border-collapse:collapse;
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
            tr:nth-child(even) {
                background:#f9f9f9;
            }
            a {
                text-decoration:none;
                color:#2c5364;
                font-weight:bold;
            }
            .back {
                margin-top:20px;
                display:inline-block;
            }
        </style>
    </head>
    <body>
    <div class="container">
    """);

        out.println("<h2>All Rooms</h2>");

        out.println("<table>");
        out.println("<tr><th>ID</th><th>Room Type</th><th>Price</th></tr>");

        for (Room room : rooms) {
            out.println("<tr>");
            out.println("<td>" + room.getRoomId() + "</td>");
            out.println("<td>" + room.getRoomType() + "</td>");
            out.println("<td>" + room.getPricePerNight() + "</td>");
            out.println("</tr>");
        }

        out.println("</table>");

        out.println("<a class='back' href='" + request.getContextPath() + "/reserve'>← Back</a>");
        out.println("</div></body></html>");
    }



    private void renderResults(HttpServletResponse response,
                               HttpServletRequest request,
                               String title,
                               List<Reservation> results)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");

        var out = response.getWriter();

        out.println("""
    <html>
    <head>
        <title>Reservations</title>
        <style>
            body {
                font-family: Segoe UI, Arial;
                background:#f4f7fa;
                margin:0;
                padding:40px;
            }
            .container {
                background:white;
                padding:30px;
                border-radius:10px;
                box-shadow:0 5px 20px rgba(0,0,0,0.1);
                max-width:1000px;
                margin:auto;
            }
            h2 {
                color:#2c5364;
                margin-bottom:20px;
            }
            table {
                width:100%;
                border-collapse:collapse;
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
            tr:nth-child(even) {
                background:#f9f9f9;
            }
            a {
                text-decoration:none;
                color:#2c5364;
                font-weight:bold;
            }
            .back {
                margin-top:20px;
                display:inline-block;
            }
        </style>
    </head>
    <body>
    <div class="container">
    """);

        out.println("<h2>" + title + "</h2>");

        if (results.isEmpty()) {
            out.println("<p>No reservations found.</p>");
        } else {
            out.println("<table>");
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

        out.println("<a class='back' href='" + request.getContextPath() + "/reserve'>← Back</a>");
        out.println("</div></body></html>");
    }


    // =====================================================
    // HELPERS
    // =====================================================

    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null &&
                session.getAttribute("loggedUser") != null;
    }

    private Reservation buildReservationFromRequest(HttpServletRequest request) {

        int roomId = Integer.parseInt(request.getParameter("roomId"));

        Room room = new Room();
        room.setRoomId(roomId);
        room.setPricePerNight(5000);

        Reservation reservation = new Reservation();
        reservation.setGuestName(request.getParameter("guestName"));
        reservation.setAddress(request.getParameter("address"));
        reservation.setContactNumber(request.getParameter("contactNumber"));
        reservation.setRoom(room);
        reservation.setCheckInDate(LocalDate.parse(request.getParameter("checkIn")));
        reservation.setCheckOutDate(LocalDate.parse(request.getParameter("checkOut")));

        return reservation;
    }

    private void redirect(HttpServletResponse response,
                          HttpServletRequest request,
                          String path) throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }
}
