package oceanviewresort.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
