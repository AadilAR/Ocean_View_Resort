package oceanviewresort.controller;

import oceanviewresort.service.AuthService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    private static final String SIGNUP_PAGE = "/signup.html";
    private static final String LOGIN_PAGE  = "/login.html";

    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        String username        = getTrimmed(request.getParameter("username"));
        String password        = getTrimmed(request.getParameter("password"));
        String confirmPassword = getTrimmed(request.getParameter("confirmPassword"));
        String role            = getTrimmed(request.getParameter("role"));

        // 1️⃣ Validate empty fields
        if (isBlank(username) || isBlank(password)
                || isBlank(confirmPassword) || isBlank(role)) {

            redirect(response, request, SIGNUP_PAGE + "?error=empty");
            return;
        }

        // 2️⃣ Validate password match
        if (!password.equals(confirmPassword)) {
            redirect(response, request, SIGNUP_PAGE + "?error=passwordMismatch");
            return;
        }

        // 3️⃣ Validate password length (server-side safety)
        if (password.length() < 6) {
            redirect(response, request, SIGNUP_PAGE + "?error=weakPassword");
            return;
        }

        // 4️⃣ Validate role
        if (!isValidRole(role)) {
            redirect(response, request, SIGNUP_PAGE + "?error=invalidRole");
            return;
        }

        try {
            boolean registered = authService
                    .register(username, password, role.toUpperCase());

            if (registered) {
                redirect(response, request,
                        LOGIN_PAGE + "?success=registered");
            } else {
                redirect(response, request,
                        SIGNUP_PAGE + "?error=exists");
            }

        } catch (Exception e) {
            e.printStackTrace(); // Replace with logger in production
            redirect(response, request,
                    SIGNUP_PAGE + "?error=server");
        }
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

    private boolean isValidRole(String role) {
        return "STAFF".equalsIgnoreCase(role)
                || "ADMIN".equalsIgnoreCase(role);
    }

    private void redirect(HttpServletResponse response,
                          HttpServletRequest request,
                          String path) throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }
}