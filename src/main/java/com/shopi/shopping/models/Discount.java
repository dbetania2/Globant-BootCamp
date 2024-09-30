package main.java.com.shopi.shopping.models;
import java.time.LocalDate;

public class Discount {
    private double rate; // Discount rate
    private String category; // Product category
    private LocalDate startDate; // Start date of the discount
    private LocalDate endDate; // End date of the discount

    // Constructor
    public Discount(double rate, String category, LocalDate startDate, LocalDate endDate) {
        if (rate < 0 || rate > 1) {
            throw new IllegalArgumentException("Discount rate must be between 0 and 1.");
        }
        this.rate = rate;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters
    public double getRate() {
        return rate;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}