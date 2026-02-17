package oceanviewresort.controller;

import oceanviewresort.model.Reservation;
import oceanviewresort.service.ReservationService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@WebServlet("/bill")
public class BillServlet extends HttpServlet {

    private final ReservationService reservationService =
            new ReservationService();

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

        long nights = ChronoUnit.DAYS.between(
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );

        response.setContentType("text/html;charset=UTF-8");
        var out = response.getWriter();

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

                .header h1 {
                    margin: 0;
                    font-size: 26px;
                    letter-spacing: 2px;
                    font-weight: 700;
                }

                .invoice-top {
                    display: flex;
                    justify-content: space-between;
                    margin-bottom: 20px;
                    font-size: 14px;
                    font-weight: 500;
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

                th {
                    text-align: left;
                    padding: 10px 0;
                    color: #666;
                    font-size: 14px;
                    border-bottom: 2px solid #ddd;
                }

                td {
                    padding: 14px 0;
                    border-bottom: 1px solid #e5e5e5;
                    font-size: 15px;
                }

                .total-row td {
                    font-weight: 700;
                    font-size: 17px;
                    border-top: 2px solid #2c5364;
                    border-bottom: none;
                    padding-top: 18px;
                }

                .currency {
                    text-align: right;
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

                .footer-note {
                    margin-top: 35px;
                    text-align: center;
                    font-size: 13px;
                    color: #666;
                }

                @media print {
                    body {
                        padding: 0;
                        background: white;
                    }

                    .invoice {
                        box-shadow: none;
                        border: none;
                        padding: 0;
                    }

                    .print-btn {
                        display: none;
                    }
                }

            </style>
        </head>
        <body>
        <div class='invoice'>
        """);

        // HEADER
        out.println("<div class='header'>");
        out.println("<h1>Ocean View Resort</h1>");
        out.println("</div>");

        // INVOICE INFO
        out.println("<div class='invoice-top'>");
        out.println("<div>Invoice #: " + reservation.getReservationId() + "</div>");
        out.println("<div>Date: " + LocalDate.now() + "</div>");
        out.println("</div>");

        out.println("<div class='divider'></div>");

        // TABLE
        out.println("<table>");
        out.println("<tr><th>Description</th><th class='currency'>Details</th></tr>");

        out.println("<tr><td>Guest</td><td class='currency'>" + reservation.getGuestName() + "</td></tr>");
        out.println("<tr><td>Room ID</td><td class='currency'>" + reservation.getRoom().getRoomId() + "</td></tr>");
        out.println("<tr><td>Check-In</td><td class='currency'>" + reservation.getCheckInDate() + "</td></tr>");
        out.println("<tr><td>Check-Out</td><td class='currency'>" + reservation.getCheckOutDate() + "</td></tr>");
        out.println("<tr><td>Total Nights</td><td class='currency'>" + nights + "</td></tr>");

        out.println("<tr class='total-row'>");
        out.println("<td>Total Amount</td>");
        out.println("<td class='currency'>LKR " + reservation.getTotalAmount() + "</td>");
        out.println("</tr>");

        out.println("</table>");

        out.println("<button class='print-btn' onclick='window.print()'>Print Invoice</button>");

        out.println("<div class='footer-note'>Thank you for choosing Ocean View Resort</div>");

        out.println("</div></body></html>");
    }
}
