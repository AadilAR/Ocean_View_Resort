package oceanviewresort.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class StandardBillingStrategy implements BillingStrategy {

    @Override
    public double calculate(LocalDate checkIn,
                            LocalDate checkOut,
                            double ratePerNight) {

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

        if (nights <= 0) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }

        return nights * ratePerNight;
    }
}