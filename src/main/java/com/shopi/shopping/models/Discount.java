package com.shopi.shopping.models;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;


@Entity
@Table(name = "discounts")  // Maps this class to the "discounts" table in the database
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Database will automatically generate the ID
    private Long id;  // Unique identifier for the discount

    private double rate; // Discount rate

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String category; // Product category
    private String type; // Discount type
    private LocalDate startDate; // Discount start date
    private LocalDate endDate; // Discount end date

    public Discount() {
        // No-argument constructor for JPA
    }

    // Constructor
    public Discount(double rate, String category, String type, LocalDate startDate, LocalDate endDate) {
        setRate(rate); // Use the setter to apply validation
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }
        this.category = category;
        this.type = type;  // Initialize the type
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

    public String getType() {
        return type;  // Getter for the type
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    // Setters
    public void setCategory(String category) {
        this.category = category;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    // Method to set the rate of the discount
    public void setRate(double rate) {
        // Validate that the rate is between 0 and 1
        if (rate < 0 || rate > 1) {
            throw new IllegalArgumentException("Discount rate must be between 0 and 1.");
        }
        this.rate = rate; // Assign the valid rate to the field
    }

    // Method to set the type of the discount
    public void setType(String type) {
        this.type = type; // Assign the type to the field
    }

    // Equals and hashCode implementations based on rate, category, type, startDate, and endDate
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Discount)) return false;
        Discount other = (Discount) obj;
        return Double.compare(other.rate, rate) == 0 &&
                Objects.equals(category, other.category) &&
                Objects.equals(type, other.type) && // Include type in equality check
                Objects.equals(startDate, other.startDate) &&
                Objects.equals(endDate, other.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rate, category, type, startDate, endDate); // Include type in hash code
    }
}
