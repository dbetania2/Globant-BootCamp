package com.shopi.shopping.models;
import com.shopi.shopping.models.products.Product;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")  // Maps this class to the "orders" table in the database
public abstract class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Database will automatically generate the ID
    protected Long id;  // Unique identifier for the order

    @ManyToMany // Establish a Many-to-Many relationship with Discount
    @JoinTable(
            name = "order_discounts",  // Join table name
            joinColumns = @JoinColumn(name = "order_id"),  // Foreign key in order_discounts referencing orders
            inverseJoinColumns = @JoinColumn(name = "discount_id")  // Foreign key in order_discounts referencing discounts
    )
    protected List<Discount> appliedDiscounts = new ArrayList<>();  // List of applied discounts

    @ManyToMany // Establish a Many-to-Many relationship with Product
    @JoinTable(
            name = "order_products",  // Join table name
            joinColumns = @JoinColumn(name = "order_id"),  // Foreign key in order_products referencing orders
            inverseJoinColumns = @JoinColumn(name = "product_id")  // Foreign key in order_products referencing products
    )
    protected List<Product> products;  // List of products in the order

    protected double totalAmount;  // Total amount of the order

    // Constructor that initializes products and calculates the total
    public Order(List<Product> products) {
        this.products = products;  // Initialize the product list
        calculateTotal();  // Automatically calculate total on creation
    }

    // Getters and Setters
    public Long getId() {
        return id;  // Return the order ID
    }

    public List<Product> getProducts() {
        return products;  // Return the list of products
    }

    public double getTotalAmount() {
        return totalAmount;  // Return the total amount
    }

    public List<Discount> getAppliedDiscounts() {
        return appliedDiscounts;  // Return the list of applied discounts
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;  // Set the total amount
    }

    // Method to add a discount
    public void addDiscount(Discount discount) {
        if (discount.isValid()) { // Only add if the discount is valid
            appliedDiscounts.add(discount);  // Add a discount to the applied discounts list
            calculateTotal(); // Recalculate total after adding a discount
        }
    }

    // Abstract method to calculate totals
    public abstract void calculateTotal();

    // Override toString for better representation
    @Override
    public String toString() {
        return "Order ID: " + id + ", Total Amount: " + totalAmount + ", Discounts Applied: " + appliedDiscounts.size();
    }
}