package oceanviewresort.service;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ReportServiceTest {

    /**
     * Validates revenue report generation.
     */
    @Test
    void testGetRevenueReport() {
        ReportService service = new ReportService();
        List<Map<String, Object>> result = service.getRevenueReport();

        assertNotNull(result);
    }

    /**
     * Validates occupancy report generation.
     */
    @Test
    void testGetOccupancyReport() {
        ReportService service = new ReportService();
        List<Map<String, Object>> result = service.getOccupancyReport();

        assertNotNull(result);
    }

    /**
     * Validates most booked room identification.
     */
    @Test
    void testGetMostBookedRoom() {
        ReportService service = new ReportService();
        Map<String, Object> result = service.getMostBookedRoom();

        assertNotNull(result);
    }
}