<<<<<<< HEAD:src/models/Order.java
package models;

import models.products.Product;
=======
package com.shopi.shopping.models;

import com.shopi.shopping.models.products.Product;
>>>>>>> topic2formerge:src/main/java/com/shopi/shopping/models/Order.java

import java.util.List;

public abstract class Order {
    protected List<Product> products; // List of products in the order
    protected double totalAmount; // Total amount of the order
    protected double discountAmount; // New field for the discount

    // Constructor that initializes products and calculates the total
    public Order(List<Product> products) {
        this.products = products; // Initialize the product list
        calculateTotal();  // Automatically calculate total on creation
        this.discountAmount = 0; // Initialize discount to zero
    }

    // Getter for total amount
    public double getTotalAmount() {
        return totalAmount - discountAmount; // Return the total minus the discount
    }

    // Setter for total amount
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount; // Set the total amount
    }

    // Method to apply a discount
    public void applyDiscount(double discount) {
        this.discountAmount = discount; // Store the applied discount
    }

    // Abstract method to calculate totals
    public abstract void calculateTotal();
<<<<<<< HEAD:src/models/Order.java
}


=======
}
>>>>>>> topic2formerge:src/main/java/com/shopi/shopping/models/Order.java
