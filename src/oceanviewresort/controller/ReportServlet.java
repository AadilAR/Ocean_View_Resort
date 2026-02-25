package oceanviewresort.controller;

import oceanviewresort.service.ReportService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/reports")
public class ReportServlet extends HttpServlet {

    private final ReportService reportService = new ReportService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String type = request.getParameter("type");

        if ("revenue".equals(type)) {
            request.setAttribute("reportData", reportService.getRevenueReport());
        }
        else if ("occupancy".equals(type)) {
            request.setAttribute("reportData", reportService.getOccupancyReport());
        }
        else if ("popular".equals(type)) {
            request.setAttribute("reportData", reportService.getMostBookedRoom());
        }

        request.setAttribute("reportType", type);
        request.getRequestDispatcher("/reports.jsp")
                .forward(request, response);
    }
}