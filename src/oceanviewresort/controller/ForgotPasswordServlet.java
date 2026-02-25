package oceanviewresort.controller;

import oceanviewresort.dao.UserDAO;
import oceanviewresort.dao.UserDAOImpl;
import oceanviewresort.model.User;
import oceanviewresort.util.EmailUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private static final String FORGOT_PAGE = "/forgotPassword.html";

    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        String email = request.getParameter("email");

        if (email == null || email.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath()
                    + FORGOT_PAGE + "?error=empty");
            return;
        }

        User user = findByEmail(email.trim());

        if (user == null) {
            response.sendRedirect(request.getContextPath()
                    + FORGOT_PAGE + "?error=notfound");
            return;
        }

        try {
            // Generate reset token
            String token = UUID.randomUUID().toString();

            user.setVerificationToken(token);
            userDAO.saveResetToken(user.getUserId(), token);

            // Create reset link
            String resetLink = request.getScheme() + "://"
                    + request.getServerName() + ":"
                    + request.getServerPort()
                    + request.getContextPath()
                    + "/resetPassword.html?token=" + token;

            // Send email
            EmailUtil.sendEmail(
                    user.getEmail(),
                    "Ocean View Resort - Password Reset",
                    "Click the link below to reset your password:\n\n"
                            + resetLink
            );

            response.sendRedirect(request.getContextPath()
                    + FORGOT_PAGE + "?success=sent");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath()
                    + FORGOT_PAGE + "?error=server");
        }
    }

    // -------------------------------------
    // Helper Method (Find by Email)
    // -------------------------------------

    private User findByEmail(String email) {

        try {
            return userDAO.findByEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}