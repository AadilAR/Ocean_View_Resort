package oceanviewresort.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/help")
public class HelpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.getWriter().println("Welcome to the Ocean View Resort Reservation System.");
        response.getWriter().println("Use the login page to authenticate.");
        response.getWriter().println("Use the reservation page to book rooms.");
    }
}
