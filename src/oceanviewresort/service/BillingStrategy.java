package oceanviewresort.service;

import java.time.LocalDate;

public interface BillingStrategy {

    double calculate(LocalDate checkIn,
                     LocalDate checkOut,
                     double ratePerNight);

}