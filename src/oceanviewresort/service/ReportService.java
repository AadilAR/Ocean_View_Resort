package oceanviewresort.service;

import oceanviewresort.dao.ReservationDAO;
import oceanviewresort.dao.ReservationDAOImpl;

import java.util.List;
import java.util.Map;

public class ReportService {

    private final ReservationDAO reservationDAO;

    public ReportService() {
        this.reservationDAO = new ReservationDAOImpl();
    }

    // =========================
    // Revenue Report
    // =========================
    public List<Map<String, Object>> getRevenueReport() {
        return reservationDAO.getMonthlyRevenue();
    }

    // =========================
    // Occupancy Report
    // =========================
    public List<Map<String, Object>> getOccupancyReport() {
        return reservationDAO.getMonthlyOccupancy();
    }

    // =========================
    // Most Booked Room Report
    // =========================
    public Map<String, Object> getMostBookedRoom() {
        return reservationDAO.getMostBookedRoom();
    }
}