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

        String username        = trim(request.getParameter("username"));
        String password        = trim(request.getParameter("password"));
        String confirmPassword = trim(request.getParameter("confirmPassword"));
        String role            = trim(request.getParameter("role"));
        String securityQuestion =
                trim(request.getParameter("securityQuestion"));
        String securityAnswer  =
                trim(request.getParameter("securityAnswer"));

        // =============================
        // VALIDATION
        // =============================

        if (isBlank(username)
                || isBlank(password) || isBlank(confirmPassword)
                || isBlank(role) || isBlank(securityQuestion)
                || isBlank(securityAnswer)) {

            redirect(response, request, SIGNUP_PAGE + "?error=empty");
            return;
        }

        if (!password.equals(confirmPassword)) {
            redirect(response, request, SIGNUP_PAGE + "?error=passwordMismatch");
            return;
        }

        if (password.length() < 6) {
            redirect(response, request, SIGNUP_PAGE + "?error=weakPassword");
            return;
        }

        if (!isValidRole(role)) {
            redirect(response, request, SIGNUP_PAGE + "?error=invalidRole");
            return;
        }

        // =============================
        // REGISTER
        // =============================

        try {

            boolean registered = authService
                    .register(username, password,
                              role.toUpperCase(),
                              securityQuestion, securityAnswer);

            if (registered) {
                redirect(response, request,
                        LOGIN_PAGE + "?success=registered");
            } else {
                redirect(response, request,
                        SIGNUP_PAGE + "?error=exists");
            }

        } catch (Exception e) {
            e.printStackTrace(); // replace with logger later
            redirect(response, request,
                    SIGNUP_PAGE + "?error=server");
        }
    }

    // =====================================
    // Utility Methods
    // =====================================

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
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
