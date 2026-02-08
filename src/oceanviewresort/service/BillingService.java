package oceanviewresort.service;

import java.time.temporal.ChronoUnit;
import java.time.LocalDate;

public class BillingService {

    public double calculateTotal(LocalDate checkIn, LocalDate checkOut, double pricePerNight) {

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

        if (nights <= 0) {
            throw new IllegalArgumentException("Invalid date range");
        }

        return nights * pricePerNight;
    }
}
