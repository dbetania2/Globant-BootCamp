package com.shopi.shopping.models;
import com.shopi.shopping.models.products.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Order {

    protected String id; // Unique identifier for the order

    protected List<Product> products; // List of products in the order
    protected double totalAmount; // Total amount of the order
    protected List<Discount> appliedDiscounts; // List of applied discounts

    // Constructor that initializes products and calculates the total
    public Order(List<Product> products) {
        this.id = UUID.randomUUID().toString(); // Generate a unique ID
        this.products = products; // Initialize the product list
        calculateTotal();  // Automatically calculate total on creation
        this.appliedDiscounts = new ArrayList<>(); // Initialize discount to zero
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products; // Return the list of products
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public List<Discount> getAppliedDiscounts() {
        return appliedDiscounts; // Return the list of applied discounts
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    // Method to add a discount
    public void addDiscount(Discount discount) {
        appliedDiscounts.add(discount); // Add a discount to the applied discounts list
    }

    // Abstract method to calculate totals
    public abstract void calculateTotal();

    // Override toString for better representation
    @Override
    public String toString() {
        return "Order ID: " + id + ", Total Amount: " + totalAmount + ", Discounts Applied: " + appliedDiscounts.size();
    }
}