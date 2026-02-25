package oceanviewresort.controller;

import oceanviewresort.service.AuthService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/verify")
public class VerifyServlet extends HttpServlet {

    private static final String LOGIN_PAGE = "/login.html";
    private static final String SIGNUP_PAGE = "/signup.html";

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        String token = getTrimmed(request.getParameter("token"));

        // =============================
        // Validate Token
        // =============================
        if (isBlank(token)) {
            redirect(response, request,
                    SIGNUP_PAGE + "?error=invalidToken");
            return;
        }

        try {

            boolean verified = authService.verifyAccount(token);

            if (verified) {
                redirect(response, request,
                        LOGIN_PAGE + "?success=verified");
            } else {
                redirect(response, request,
                        SIGNUP_PAGE + "?error=invalidOrExpired");
            }

        } catch (Exception e) {
            e.printStackTrace(); // Replace with logger later
            redirect(response, request,
                    SIGNUP_PAGE + "?error=server");
        }
    }

    // =============================
    // Helper Methods
    // =============================

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