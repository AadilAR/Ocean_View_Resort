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
        String email           = getTrimmed(request.getParameter("email"));
        String password        = getTrimmed(request.getParameter("password"));
        String confirmPassword = getTrimmed(request.getParameter("confirmPassword"));
        String role            = getTrimmed(request.getParameter("role"));

        // =============================
        // VALIDATION
        // =============================

        if (isBlank(username) || isBlank(email)
                || isBlank(password) || isBlank(confirmPassword)
                || isBlank(role)) {

            redirect(response, request, SIGNUP_PAGE + "?error=empty");
            return;
        }

        if (!isValidEmail(email)) {
            redirect(response, request, SIGNUP_PAGE + "?error=invalidEmail");
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

            boolean registered = authService.register(
                    username,
                    email,
                    password,
                    role.toUpperCase()
            );

            if (registered) {
                redirect(response, request,
                        LOGIN_PAGE + "?success=verifyEmail");
            } else {
                request.setAttribute("message", "Existing email");
                request.setAttribute("type", "error");
                request.getRequestDispatcher("/message.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            redirect(response, request,
                    SIGNUP_PAGE + "?error=server");
        }
    }

    // =====================================
    // Helper Methods
    // =====================================

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

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private void redirect(HttpServletResponse response,
                          HttpServletRequest request,
                          String path) throws IOException {

        response.sendRedirect(request.getContextPath() + path);
    }
}