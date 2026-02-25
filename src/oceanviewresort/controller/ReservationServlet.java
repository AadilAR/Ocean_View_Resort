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

        // 🔒 Prevent browser caching
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
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

        // 🔒 Prevent browser caching
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
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
            * {
                box-sizing: border-box;
            }

            body {
                margin: 0;
                font-family: "Segoe UI", Arial, sans-serif;
                background-image: url('images/background.jpg');
                background-size: cover;
                background-position: center;
                background-attachment: fixed;
                position: relative;
            }

            /* Dark overlay */
            body::before {
                content: "";
                position: fixed;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.55);
                backdrop-filter: blur(4px);
                z-index: 0;
            }

            .header {
                position: relative;
                z-index: 1;
                text-align: center;
                padding: 25px;
                font-size: 28px;
                font-weight: 700;
                letter-spacing: 2px;
                background: linear-gradient(90deg, #ffffff, #d4af37);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
            }

            .wrapper {
                position: relative;
                z-index: 1;
                display: flex;
                gap: 40px;
                padding: 40px;
            }

            .box {
                background: rgba(255,255,255,0.15);
                backdrop-filter: blur(18px);
                -webkit-backdrop-filter: blur(18px);
                border: 1px solid rgba(255,255,255,0.25);
                padding: 30px;
                border-radius: 18px;
                box-shadow: 0 15px 40px rgba(0,0,0,0.4);
                color: white;
            }

            .form-box { width: 420px; }
            .room-box { flex: 1; }

            h2 {
                text-align: center;
                margin-bottom: 25px;
                font-weight: 600;
                color: #ffffff;
            }

            input {
                width: 100%;
                padding: 10px;
                margin: 10px 0;
                border-radius: 8px;
                border: none;
                outline: none;
            }

            input:focus {
                box-shadow: 0 0 10px rgba(255,255,255,0.6);
            }

            button {
                padding: 10px;
                background: linear-gradient(135deg, #2c5364, #203a43);
                color: white;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                margin-top: 10px;
                width: 100%;
                transition: 0.3s ease;
            }

            button:hover {
                transform: translateY(-2px);
                box-shadow: 0 8px 20px rgba(0,0,0,0.4);
            }

            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 15px;
            }

            th, td {
                border: 1px solid rgba(255,255,255,0.3);
                padding: 10px;
                text-align: center;
            }

            th {
                background: rgba(44,83,100,0.9);
                color: white;
            }

            td {
                background: rgba(255,255,255,0.08);
            }

            .success {
                color: #00ffae;
                margin-top: 15px;
            }

            .error {
                color: #ff6b6b;
                margin-top: 10px;
            }

            .help-btn {
                position: fixed;
                bottom: 25px;
                right: 25px;
                width: 55px;
                height: 55px;
                background: linear-gradient(135deg, #2c5364, #1e3c50);
                color: white;
                font-size: 24px;
                font-weight: bold;
                text-align: center;
                line-height: 55px;
                border-radius: 50%;
                text-decoration: none;
                box-shadow: 0 6px 18px rgba(0,0,0,0.4);
                z-index: 999;
            }

            .help-btn:hover {
                transform: scale(1.1);
            }
        </style>
    </head>
    <body>
    <div class='header'>Ocean View Resort</div>
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

        out.println("<form method='post' action='" + request.getContextPath() + "/reserve'>");
        out.println("<input type='text' name='guestName' placeholder='Guest Name' required>");
        out.println("<input type='text' name='address' placeholder='Address'>");
        out.println("<input type='text' name='contactNumber' placeholder='Contact Number' required>");
        out.println("<input type='number' name='roomId' placeholder='Room ID' required>");
        out.println("<input type='date' name='checkIn' required>");
        out.println("<input type='date' name='checkOut' required>");
        out.println("<button type='submit'>Reserve</button>");

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
        out.println("</div>");
        out.println("<a href='help' class='help-btn'>?</a>");
        out.println("</body></html>");
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
            * {
                box-sizing: border-box;
            }

            body {
                margin: 0;
                font-family: "Segoe UI", Arial, sans-serif;
                background-image: url('images/background.jpg');
                background-size: cover;
                background-position: center;
                background-attachment: fixed;
                position: relative;
            }

            /* Dark overlay */
            body::before {
                content: "";
                position: fixed;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.55);
                backdrop-filter: blur(4px);
                z-index: 0;
            }

            .header {
                position: relative;
                z-index: 1;
                text-align: center;
                padding: 25px;
                font-size: 28px;
                font-weight: 700;
                letter-spacing: 2px;
                background: linear-gradient(90deg, #ffffff, #d4af37);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
            }

            .container {
                position: relative;
                z-index: 1;
                max-width: 900px;
                margin: 60px auto;
                padding: 35px;
                border-radius: 18px;

                background: rgba(255,255,255,0.15);
                backdrop-filter: blur(18px);
                -webkit-backdrop-filter: blur(18px);
                border: 1px solid rgba(255,255,255,0.25);
                box-shadow: 0 15px 40px rgba(0,0,0,0.4);

                color: white;
            }

            h2 {
                text-align: center;
                margin-bottom: 25px;
                font-weight: 600;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 15px;
            }

            th, td {
                padding: 12px;
                text-align: center;
                border: 1px solid rgba(255,255,255,0.3);
            }

            th {
                background: rgba(44,83,100,0.9);
                color: white;
            }

            td {
                background: rgba(255,255,255,0.08);
            }

            tr:hover td {
                background: rgba(255,255,255,0.15);
                transition: 0.3s ease;
            }

            .back {
                display: inline-block;
                margin-top: 25px;
                text-decoration: none;
                padding: 10px 18px;
                border-radius: 8px;
                background: linear-gradient(135deg, #2c5364, #203a43);
                color: white;
                font-weight: 500;
                transition: 0.3s ease;
            }

            .back:hover {
                transform: translateY(-2px);
                box-shadow: 0 8px 20px rgba(0,0,0,0.4);
            }

            .help-btn {
                position: fixed;
                bottom: 25px;
                right: 25px;
                width: 55px;
                height: 55px;
                background: linear-gradient(135deg, #2c5364, #1e3c50);
                color: white;
                font-size: 24px;
                font-weight: bold;
                text-align: center;
                line-height: 55px;
                border-radius: 50%;
                text-decoration: none;
                box-shadow: 0 6px 18px rgba(0,0,0,0.4);
                z-index: 999;
            }

            .help-btn:hover {
                transform: scale(1.1);
            }
        </style>
    </head>
    <body>
        <div class="header">Ocean View Resort</div>
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

        out.println("<a class='back' href='"
                + request.getContextPath()
                + "/reserve'>← Back to Reservation</a>");

        out.println("</div>");
        out.println("<a href='help' class='help-btn'>?</a>");
        out.println("</body></html>");
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
            * {
                box-sizing: border-box;
            }

            body {
                margin: 0;
                font-family: "Segoe UI", Arial, sans-serif;
                background-image: url('images/background.jpg');
                background-size: cover;
                background-position: center;
                background-attachment: fixed;
                position: relative;
            }

            /* Dark overlay */
            body::before {
                content: "";
                position: fixed;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0.55);
                backdrop-filter: blur(4px);
                z-index: 0;
            }

            .header {
                position: relative;
                z-index: 1;
                text-align: center;
                padding: 25px;
                font-size: 28px;
                font-weight: 700;
                letter-spacing: 2px;
                background: linear-gradient(90deg, #ffffff, #d4af37);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
            }

            .container {
                position: relative;
                z-index: 1;
                max-width: 1100px;
                margin: 60px auto;
                padding: 35px;
                border-radius: 18px;

                background: rgba(255,255,255,0.15);
                backdrop-filter: blur(18px);
                -webkit-backdrop-filter: blur(18px);
                border: 1px solid rgba(255,255,255,0.25);
                box-shadow: 0 15px 40px rgba(0,0,0,0.4);

                color: white;
            }

            h2 {
                text-align: center;
                margin-bottom: 25px;
                font-weight: 600;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
            }

            th, td {
                padding: 12px;
                text-align: center;
                border: 1px solid rgba(255,255,255,0.3);
            }

            th {
                background: rgba(44,83,100,0.9);
                color: white;
            }

            td {
                background: rgba(255,255,255,0.08);
            }

            tr:hover td {
                background: rgba(255,255,255,0.15);
                transition: 0.3s ease;
            }

            .back {
                display: inline-block;
                margin-top: 25px;
                padding: 10px 18px;
                border-radius: 8px;
                background: linear-gradient(135deg, #2c5364, #203a43);
                color: white;
                text-decoration: none;
                font-weight: 500;
                transition: 0.3s ease;
            }

            .back:hover {
                transform: translateY(-2px);
                box-shadow: 0 8px 20px rgba(0,0,0,0.4);
            }

            .help-btn {
                position: fixed;
                bottom: 25px;
                right: 25px;
                width: 55px;
                height: 55px;
                background: linear-gradient(135deg, #2c5364, #1e3c50);
                color: white;
                font-size: 24px;
                font-weight: bold;
                text-align: center;
                line-height: 55px;
                border-radius: 50%;
                text-decoration: none;
                box-shadow: 0 6px 18px rgba(0,0,0,0.4);
                z-index: 999;
            }

            .help-btn:hover {
                transform: scale(1.1);
            }
        </style>
    </head>
    <body>
        <div class="header">Ocean View Resort</div>
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

        out.println("<a class='back' href='" + request.getContextPath() + "/reserve'>← Back to Reservation</a>");
        out.println("</div>");
        out.println("<a href='help' class='help-btn'>?</a>");
        out.println("</body></html>");
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
