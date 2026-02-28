package oceanviewresort.controller;

import oceanviewresort.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/verify")
public class VerifyServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException, ServletException {

        String token = getTrimmed(request.getParameter("token"));

        // =============================
        // Validate Token
        // =============================
        if (isBlank(token)) {
            showMessage(request, response,
                    "Invalid or expired token.",
                    "error");
            return;
        }

        try {

            boolean verified = authService.verifyAccount(token);

            if (verified) {
                showMessage(request, response,
                        "Account successfully verified.",
                        "success");
            } else {
                showMessage(request, response,
                        "Invalid or expired token.",
                        "error");
            }

        } catch (Exception e) {
            e.printStackTrace(); // Replace with logger later
            showMessage(request, response,
                    "Server error. Please try again later.",
                    "error");
        }
    }

    // =============================
    // Helper Methods
    // =============================

    private void showMessage(HttpServletRequest request,
                             HttpServletResponse response,
                             String message,
                             String type)
            throws ServletException, IOException {

        request.setAttribute("message", message);
        request.setAttribute("type", type);
        request.getRequestDispatcher("/message.jsp")
                .forward(request, response);
    }

    private String getTrimmed(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }
}