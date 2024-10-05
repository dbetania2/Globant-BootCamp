package com.shopi.shopping.models;
import java.time.LocalDate;

public class Discount {
    private double rate; // Discount rate
    private String category; // Product category
    private LocalDate startDate; // Discount start date
    private LocalDate endDate; // Discount end date

    // Constructor
    public Discount(double rate, String category, LocalDate startDate, LocalDate endDate) {
        if (rate < 0 || rate > 1) {
            throw new IllegalArgumentException("Discount rate must be between 0 and 1.");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
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

    // Method to check if the discount is valid on a given date
    public boolean isValid() {
        LocalDate currentDate = LocalDate.now();
        return !currentDate.isBefore(startDate) && !currentDate.isAfter(endDate);
    }
}