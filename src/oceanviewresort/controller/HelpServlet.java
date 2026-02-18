package oceanviewresort.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/help")
public class HelpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

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

                .subtitle {
                    text-align: center;
                    margin: -18px auto 22px auto;
                    max-width: 760px;
                    opacity: 0.92;
                    line-height: 1.6;
                    font-size: 14px;
                }

                .toc {
                    display: flex;
                    flex-wrap: wrap;
                    gap: 10px;
                    justify-content: center;
                    margin: 16px 0 26px 0;
                }

                .toc a {
                    text-decoration: none;
                    color: white;
                    padding: 10px 14px;
                    border-radius: 12px;
                    background: rgba(255,255,255,0.12);
                    border: 1px solid rgba(255,255,255,0.18);
                    transition: 0.2s ease;
                    font-size: 13px;
                    font-weight: 600;
                }

                .toc a:hover {
                    background: rgba(255,255,255,0.2);
                    transform: translateY(-1px);
                }

                .section {
                    margin-top: 22px;
                    padding-top: 6px;
                }

                .section-title {
                    margin: 26px 0 10px 0;
                    font-size: 20px;
                    font-weight: 800;
                    letter-spacing: 0.5px;
                }

                .card {
                    background: rgba(255,255,255,0.10);
                    border: 1px solid rgba(255,255,255,0.18);
                    border-radius: 16px;
                    padding: 18px 18px 16px 18px;
                    margin: 12px 0;
                }

                .card h3 {
                    margin: 0 0 10px 0;
                    font-size: 16px;
                    font-weight: 700;
                    color: #ffffff;
                }

                .hint {
                    margin-top: 10px;
                    font-size: 13px;
                    opacity: 0.9;
                    line-height: 1.6;
                }

                ul {
                    margin: 0 0 10px 20px;
                    line-height: 1.8;
                    font-size: 15px;
                }

                li {
                    margin-bottom: 6px;
                }

                .link-inline {
                    color: #ffffff;
                    text-decoration: underline;
                    opacity: 0.95;
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

        <div class="subtitle">
            Quick steps for staff accounts and reservations. Password reset uses your <b>Security Question</b>
            (no email verification / no reset link).
        </div>

        <div class="toc">
            <a href="#accounts">Accounts</a>
            <a href="#reset-password">Reset Password</a>
            <a href="#reservations">Reservations</a>
            <a href="#billing">Billing</a>
        </div>

        <div class="section" id="accounts">
            <div class="section-title">Accounts</div>

            <div class="card">
                <h3>Register New Staff</h3>
                <ul>
                    <li>Go to the Login page</li>
                    <li>Click <b>Create Staff Account</b></li>
                    <li>Enter <b>username</b>, <b>password</b>, <b>role</b>, and choose a <b>security question</b></li>
                    <li>Enter your <b>security answer</b> (keep it private)</li>
                    <li>Click <b>Create Account</b></li>
                </ul>
            </div>

            <div class="card">
                <h3>Login</h3>
                <ul>
                    <li>Enter your username and password</li>
                    <li>Click <b>Login</b></li>
                </ul>
            </div>
        </div>

        <div class="section" id="reset-password">
            <div class="section-title">Reset Password (Security Question)</div>

            <div class="card">
                <h3>How to reset your password</h3>
                <ul>
                    <li>Open the Login page</li>
                    <li>Click <a class="link-inline" href="forgotPassword.html">Forgot Password?</a></li>
                    <li>Enter your <b>username</b> and click <b>Recover Password</b></li>
                    <li>The system will display your <b>security question</b></li>
                    <li>Enter the correct <b>answer</b>, then type your new password and confirm it</li>
                    <li>Click <b>Update Password</b>, then login with the new password</li>
                </ul>
                <div class="hint">
                    Notes: The security answer must match what you set during registration. If you forget your answer,
                    contact an admin to reset it in the database.
                </div>
            </div>
        </div>

        <div class="section" id="reservations">
            <div class="section-title">Reservations</div>

            <div class="card">
                <h3>Make a Reservation</h3>
                <ul>
                    <li>Fill Guest Name, Address, Contact Number</li>
                    <li>Enter Room ID (check Room table)</li>
                    <li>Select Check-in and Check-out dates</li>
                    <li>Click <b>Reserve</b></li>
                    <li>You will be redirected to Printable Bill</li>
                </ul>
            </div>

            <div class="card">
                <h3>View Room Details</h3>
                <ul>
                    <li>View Rooms & Bookings table</li>
                    <li>See Room ID, Type, Price and Booked Dates</li>
                </ul>
            </div>

            <div class="card">
                <h3>Search Reservations</h3>
                <ul>
                    <li>Enter mobile number in the search field</li>
                    <li>Click <b>Search</b></li>
                    <li>System displays matching reservations</li>
                </ul>
            </div>

            <div class="card">
                <h3>View All Reservations</h3>
                <ul>
                    <li>Click <b>View All Reservations</b></li>
                    <li>System displays all bookings</li>
                </ul>
            </div>

            <div class="card">
                <h3>Cancel Reservation</h3>
                <ul>
                    <li>Search reservation or view all</li>
                    <li>Click <b>Cancel</b> next to the reservation</li>
                </ul>
            </div>
        </div>

        <div class="section" id="billing">
            <div class="section-title">Billing</div>

            <div class="card">
                <h3>Print Bill</h3>
                <ul>
                    <li>After successful booking, bill page opens</li>
                    <li>Click <b>Print</b> to generate the invoice</li>
                </ul>
            </div>
        </div>

        <div class="back">
            <a href="javascript:history.back()">← Back to Previous Page</a>
        </div>

        </div>

        </body>
        </html>
        """);
    }
}
