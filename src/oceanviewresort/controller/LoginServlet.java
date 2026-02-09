package oceanviewresort.controller;

import oceanviewresort.model.User;
import oceanviewresort.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = authService.authenticate(username, password);

        if (user != null) {
            // Successful login → redirect to reservation page
            response.sendRedirect("web/reservation.html");
        } else {
            // Failed login → redirect back to login page
            response.sendRedirect("web/login.html");
        }
    }
}
