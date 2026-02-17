package oceanviewresort.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/help")
public class HelpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");
        var out = response.getWriter();

        out.println("""
        <html>
        <head>
            <title>Help - Ocean View Resort</title>
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
                    color: white;
                }

                /* Dark overlay */
                body::before {
                    content: "";
                    position: fixed;
                    width: 100%;
                    height: 100%;
                    background: rgba(0,0,0,0.6);
                    backdrop-filter: blur(6px);
                    z-index: 0;
                }

                .container {
                    position: relative;
                    z-index: 1;

                    max-width: 950px;
                    margin: 60px auto;
                    padding: 40px;
                    border-radius: 18px;

                    background: rgba(255,255,255,0.15);
                    backdrop-filter: blur(18px);
                    -webkit-backdrop-filter: blur(18px);

                    border: 1px solid rgba(255,255,255,0.25);
                    box-shadow: 0 15px 40px rgba(0,0,0,0.4);
                }

                h1 {
                    text-align: center;
                    font-size: 32px;
                    margin-bottom: 35px;
                    font-weight: 700;
                    letter-spacing: 1px;

                    background: linear-gradient(90deg, #ffffff, #d4af37);
                    -webkit-background-clip: text;
                    -webkit-text-fill-color: transparent;
                }

                h3 {
                    margin-top: 28px;
                    margin-bottom: 10px;
                    font-size: 18px;
                    font-weight: 600;
                    color: #ffffff;
                }

                ul {
                    margin: 0 0 10px 20px;
                    line-height: 1.8;
                    font-size: 15px;
                }

                li {
                    margin-bottom: 6px;
                }

                .back {
                    margin-top: 40px;
                    text-align: center;
                }

                .back a {
                    padding: 12px 25px;
                    border-radius: 10px;
                    text-decoration: none;
                    font-weight: 600;
                    background: linear-gradient(135deg, #2c5364, #203a43);
                    color: white;
                    transition: 0.3s ease;
                }

                .back a:hover {
                    transform: translateY(-2px);
                    box-shadow: 0 8px 20px rgba(0,0,0,0.4);
                }

                @media (max-width: 768px) {
                    .container {
                        margin: 20px;
                        padding: 25px;
                    }
                }

            </style>
        </head>
        <body>

        <div class="container">

        <h1>Ocean View Resort - Help Guide</h1>

        <h3>1. Register New Staff</h3>
        <ul>
            <li>Go to Login page</li>
            <li>Click "Create Staff Account"</li>
            <li>Enter username, password and role</li>
            <li>Click Create Account</li>
        </ul>

        <h3>2. Login</h3>
        <ul>
            <li>Enter your username and password</li>
            <li>Click Login</li>
        </ul>

        <h3>3. Make a Reservation</h3>
        <ul>
            <li>Fill Guest Name, Address, Contact Number</li>
            <li>Enter Room ID (check Room table)</li>
            <li>Select Check-in and Check-out dates</li>
            <li>Click Reserve</li>
            <li>You will be redirected to Printable Bill</li>
        </ul>

        <h3>4. View Room Details</h3>
        <ul>
            <li>View Rooms & Bookings table</li>
            <li>See Room ID, Type, Price and Booked Dates</li>
        </ul>

        <h3>5. Search Reservations</h3>
        <ul>
            <li>Enter mobile number in search field</li>
            <li>Click Search</li>
            <li>System displays matching reservations</li>
        </ul>

        <h3>6. View All Reservations</h3>
        <ul>
            <li>Click "View All Reservations"</li>
            <li>System displays all bookings</li>
        </ul>

        <h3>7. Cancel Reservation</h3>
        <ul>
            <li>Search reservation or view all</li>
            <li>Click Cancel button next to reservation</li>
        </ul>

        <h3>8. Print Bill</h3>
        <ul>
            <li>After successful booking, bill page opens</li>
            <li>Click Print to generate invoice</li>
        </ul>

        <div class="back">
            <a href="javascript:history.back()">← Back to Previous Page</a>
        </div>

        </div>

        </body>
        </html>
        """);
    }
}
