package oceanviewresort.controller;

import oceanviewresort.model.Reservation;
import oceanviewresort.model.User;
import oceanviewresort.service.*;
import oceanviewresort.dao.RoomDAO;
import oceanviewresort.dao.RoomDAOImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.text.DecimalFormat;

@WebServlet("/bill")
public class BillServlet extends HttpServlet {

    private final ReservationService reservationService =
            new ReservationService();

    private final RoomDAO roomDAO =
            new RoomDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        String idParam = request.getParameter("reservationId");

        if (idParam == null) {
            response.sendRedirect("reserve");
            return;
        }

        int id = Integer.parseInt(idParam);

        Reservation reservation =
                reservationService.getReservationById(id);

        if (reservation == null) {
            response.sendRedirect("reserve");
            return;
        }

        // ==========================
        // Retrieve Data
        // ==========================

        LocalDate checkIn = reservation.getCheckInDate();
        LocalDate checkOut = reservation.getCheckOutDate();

        int roomId = reservation.getRoom().getRoomId();
        double ratePerNight = roomDAO.getPriceByRoomId(roomId);

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

        // ==========================
        // Strategy Pattern Selection
        // ==========================

        BillingStrategy strategy =
                (nights > 5)
                        ? new DiscountBillingStrategy()
                        : new StandardBillingStrategy();

        BillingService billingService =
                new BillingService(strategy);

        double subtotal = nights * ratePerNight;

        double totalAmount =
                billingService.calculateTotal(checkIn, checkOut, ratePerNight);

        double discountAmount = subtotal - totalAmount;

        DecimalFormat df = new DecimalFormat("#,##0.00");

        // ==========================
// SEND EMAIL (ONLY ONCE)
// ==========================

        HttpSession session = request.getSession();

        if (reservation.getEmail() != null &&
                !reservation.getEmail().isEmpty() &&
                session.getAttribute("invoiceSent_" + id) == null) {

            EmailService emailService = new SmtpEmailService();

            String emailMessage =
                    "<h2>Ocean View Resort - Invoice</h2>" +
                            "<p><b>Invoice #:</b> " + reservation.getReservationId() + "</p>" +
                            "<p><b>Date:</b> " + LocalDate.now() + "</p>" +
                            "<hr>" +
                            "<p><b>Guest:</b> " + reservation.getGuestName() + "</p>" +
                            "<p><b>Room ID:</b> " + roomId + "</p>" +
                            "<p><b>Check-In:</b> " + checkIn + "</p>" +
                            "<p><b>Check-Out:</b> " + checkOut + "</p>" +
                            "<p><b>Total Nights:</b> " + nights + "</p>" +
                            "<p><b>Subtotal:</b> LKR " + df.format(subtotal) + "</p>";

            if (discountAmount > 0) {
                emailMessage += "<p><b>Discount (10%):</b> - LKR "
                        + df.format(discountAmount) + "</p>";
            }

            emailMessage += "<h3>Total Amount: LKR "
                    + df.format(totalAmount) + "</h3>" +
                    "<p>Thank you for staying with Ocean View Resort!</p>";

            emailService.sendBillReceipt(
                    reservation.getEmail(),   // ✅ NOW SENDS TO GUEST
                    "Ocean View Resort Invoice #" + reservation.getReservationId(),
                    emailMessage
            );

            session.setAttribute("invoiceSent_" + id, true);
        }

        // ==========================
        // Generate Invoice HTML Page
        // ==========================

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("""
        <html>
        <head>
            <title>Invoice - Ocean View Resort</title>
            <style>
                body {
                    font-family: "Segoe UI", Arial, sans-serif;
                    margin: 0;
                    padding: 30px;
                    background: #f4f7fa;
                }
                .invoice {
                    max-width: 750px;
                    margin: auto;
                    padding: 35px;
                    border-radius: 10px;
                    background: white;
                    box-shadow: 0 10px 25px rgba(0,0,0,0.15);
                }
                .header {
                    text-align: center;
                    margin-bottom: 25px;
                }
                .divider {
                    height: 2px;
                    background: #2c5364;
                    margin: 15px 0 25px 0;
                }
                table {
                    width: 100%;
                    border-collapse: collapse;
                }
                th, td {
                    padding: 12px 0;
                }
                th {
                    text-align: left;
                    border-bottom: 2px solid #ddd;
                }
                .currency {
                    text-align: right;
                }
                .total-row td {
                    font-weight: 700;
                    font-size: 17px;
                    border-top: 2px solid #2c5364;
                }
                .print-btn {
                    margin-top: 25px;
                    padding: 12px 25px;
                    border: none;
                    border-radius: 6px;
                    background: #2c5364;
                    color: white;
                    font-weight: 600;
                    cursor: pointer;
                }
            </style>
        </head>
        <body>
        <div class='invoice'>
        """);

        out.println("<div class='header'><h1>Ocean View Resort</h1></div>");
        out.println("<div>Invoice #: " + reservation.getReservationId() + "</div>");
        out.println("<div>Date: " + LocalDate.now() + "</div>");
        out.println("<div class='divider'></div>");

        out.println("<table>");
        out.println("<tr><th>Description</th><th class='currency'>Details</th></tr>");
        out.println("<tr><td>Guest</td><td class='currency'>" + reservation.getGuestName() + "</td></tr>");
        out.println("<tr><td>Room ID</td><td class='currency'>" + roomId + "</td></tr>");
        out.println("<tr><td>Check-In</td><td class='currency'>" + checkIn + "</td></tr>");
        out.println("<tr><td>Check-Out</td><td class='currency'>" + checkOut + "</td></tr>");
        out.println("<tr><td>Total Nights</td><td class='currency'>" + nights + "</td></tr>");

        out.println("<tr>");
        out.println("<td>Subtotal</td>");
        out.println("<td class='currency'>LKR " + df.format(subtotal) + "</td>");
        out.println("</tr>");

        if (discountAmount > 0) {
            out.println("<tr>");
            out.println("<td>Discount (10%)</td>");
            out.println("<td class='currency'>- LKR " + df.format(discountAmount) + "</td>");
            out.println("</tr>");
        }

        out.println("<tr class='total-row'>");
        out.println("<td>Total Amount</td>");
        out.println("<td class='currency'>LKR " + df.format(totalAmount) + "</td>");
        out.println("</tr>");

        out.println("</table>");
        out.println("<button class='print-btn' onclick='window.print()'>Print Invoice</button>");
        out.println("</div></body></html>");
    }
}