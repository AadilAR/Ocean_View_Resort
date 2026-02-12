package oceanviewresort.controller;

import oceanviewresort.service.AuthService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private static final String FORGOT_PAGE = "/forgotPassword.html";

    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        String username = getTrimmed(request.getParameter("username"));

        // Validate empty field
        if (isBlank(username)) {
            redirect(response, request, FORGOT_PAGE + "?error=empty");
            return;
        }

        try {
            String password = authService.getPasswordByUsername(username);

            if (password != null) {
                // Show result on same page
                redirect(response, request,
                        FORGOT_PAGE + "?success=recovered&password=" + password);
            } else {
                redirect(response, request,
                        FORGOT_PAGE + "?error=notfound");
            }

        } catch (Exception e) {
            e.printStackTrace();
            redirect(response, request,
                    FORGOT_PAGE + "?error=server");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        redirect(response, request, FORGOT_PAGE);
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