package oceanviewresort.controller;

import oceanviewresort.model.User;
import oceanviewresort.service.AuthService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String LOGIN_PAGE = "/login.html";
    private static final String RESERVATION_PAGE = "/reservation.html";

    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        String username = getTrimmed(request.getParameter("username"));
        String password = getTrimmed(request.getParameter("password"));

        // Validate empty fields
        if (isBlank(username) || isBlank(password)) {
            redirect(response, request, LOGIN_PAGE + "?error=empty");
            return;
        }

        User user = authService.authenticate(username, password);

        if (user != null) {

            // Invalidate old session (security best practice)
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            // Create new session
            HttpSession session = request.getSession(true);
            session.setAttribute("loggedUser", user);
            session.setAttribute("role", user.getRole());

            // Optional: session timeout (30 minutes)
            session.setMaxInactiveInterval(30 * 60);

            redirect(response, request, RESERVATION_PAGE);

        } else {
            redirect(response, request, LOGIN_PAGE + "?error=invalid");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        redirect(response, request, LOGIN_PAGE);
    }

    // ------------------------
    // Helper Methods
    // ------------------------

    private String getTrimmed(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }

    private void redirect(HttpServletResponse response,
                          HttpServletRequest request,
                          String path) throws IOException {

        response.sendRedirect(request.getContextPath() + path);
    }
}