package oceanviewresort.service;

import java.time.LocalDate;

public class BillingService {

    private BillingStrategy strategy;

    // Default constructor (uses Standard strategy automatically)
    public BillingService() {
        this.strategy = new StandardBillingStrategy();
    }

    // Constructor injection (for Strategy pattern usage)
    public BillingService(BillingStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        this.strategy = strategy;
    }

    // Main calculation method
    public double calculateTotal(LocalDate checkIn,
                                 LocalDate checkOut,
                                 double pricePerNight) {

        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }

        return strategy.calculate(checkIn, checkOut, pricePerNight);
    }

    // Optional: change strategy dynamically
    public void setStrategy(BillingStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        this.strategy = strategy;
    }
}