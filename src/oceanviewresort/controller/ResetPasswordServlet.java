package oceanviewresort.controller;

import oceanviewresort.dao.UserDAO;
import oceanviewresort.dao.UserDAOImpl;
import oceanviewresort.model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    private static final String RESET_PAGE = "/resetPassword.html";
    private static final String LOGIN_PAGE = "/login.html";

    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        // Simply redirect to reset page (token handled in frontend)
        response.sendRedirect(request.getContextPath() + RESET_PAGE);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        String token           = request.getParameter("token");
        String password        = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (isBlank(token) || isBlank(password) || isBlank(confirmPassword)) {
            response.sendRedirect(request.getContextPath()
                    + RESET_PAGE + "?error=empty&token=" + token);
            return;
        }

        if (!password.equals(confirmPassword)) {
            response.sendRedirect(request.getContextPath()
                    + RESET_PAGE + "?error=passwordMismatch&token=" + token);
            return;
        }

        if (password.length() < 6) {
            response.sendRedirect(request.getContextPath()
                    + RESET_PAGE + "?error=weakPassword&token=" + token);
            return;
        }

        try {
            User user = userDAO.findByVerificationToken(token);

            if (user == null) {
                response.sendRedirect(request.getContextPath()
                        + RESET_PAGE + "?error=invalidToken");
                return;
            }

            // Update password
            userDAO.updatePassword(user.getUserId(), password);

            // Clear token after reset
            userDAO.verifyUser(user.getUserId());
            // OR better: create a separate clearToken method

            response.sendRedirect(request.getContextPath()
                    + LOGIN_PAGE + "?success=reset");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath()
                    + RESET_PAGE + "?error=server");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}