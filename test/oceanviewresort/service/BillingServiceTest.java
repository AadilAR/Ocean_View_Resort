package oceanviewresort.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BillingServiceTest {

    @Test
    void testCalculateTotalValidDates() {

        BillingService billingService = new BillingService();

        LocalDate checkIn = LocalDate.of(2026, 3, 1);
        LocalDate checkOut = LocalDate.of(2026, 3, 5);

        double total = billingService.calculateTotal(checkIn, checkOut, 5000);

        assertEquals(20000, total);
    }

    @Test
    void testCalculateTotalInvalidDates() {

        BillingService billingService = new BillingService();

        LocalDate checkIn = LocalDate.of(2026, 3, 5);
        LocalDate checkOut = LocalDate.of(2026, 3, 3);

        assertThrows(IllegalArgumentException.class, () ->
                billingService.calculateTotal(checkIn, checkOut, 5000)
        );
    }

    /**
     * Test null dates should throw IllegalArgumentException.
     */
    @Test
    void testCalculateTotal_NullDates() {
        BillingService service = new BillingService();

        assertThrows(IllegalArgumentException.class, () ->
                service.calculateTotal(null, null, 5000)
        );
    }

    /**
     * Test standard billing calculation without discount.
     * 3 nights × 5000 = 15000
     */
    @Test
    void testCalculateTotal_StandardStrategy() {
        BillingService service = new BillingService();

        double total = service.calculateTotal(
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 4),
                5000
        );

        assertEquals(15000, total);
    }

    /**
     * Test discount billing strategy for more than 5 nights.
     * Ensures discount is applied.
     */
    @Test
    void testCalculateTotal_DiscountStrategy() {
        BillingService service =
                new BillingService(new DiscountBillingStrategy());

        double total = service.calculateTotal(
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 7),
                5000
        );

        assertTrue(total < 30000);
    }
}