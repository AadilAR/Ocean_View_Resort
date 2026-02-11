package oceanviewresort.controller;

import oceanviewresort.model.User;
import oceanviewresort.service.AuthService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Basic validation
        if (username == null || password == null ||
                username.trim().isEmpty() || password.trim().isEmpty()) {

            response.setContentType("text/html");
            response.getWriter().println("<h3>Username and password are required.</h3>");
            response.getWriter().println("<a href='login.html'>Back to Login</a>");
            return;
        }

        User user = authService.authenticate(username.trim(), password.trim());

        if (user != null) {

            // Create new session (invalidate old one for safety)
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            session = request.getSession(true);
            session.setAttribute("loggedUser", user);
            session.setAttribute("role", user.getRole());

            // Redirect to reservation page
            response.sendRedirect(request.getContextPath() + "/reservation.html");

        } else {
            response.setContentType("text/html");
            response.getWriter().println("<h3>Invalid username or password.</h3>");
            response.getWriter().println("<a href='login.html'>Try Again</a>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.sendRedirect(request.getContextPath() + "/login.html");
    }
}
