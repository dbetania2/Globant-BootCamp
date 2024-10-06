
package com.shopi.shopping.models;

import com.shopi.shopping.models.products.Product;
import jakarta.persistence.Entity;

import java.util.List;


@Entity
public class StandardOrder extends Order {

    // Constructor to initialize the order with products
    public StandardOrder(List<Product> products) {
        super(products); // Call the parent constructor to initialize products
    }

    // Implementing the abstract method to calculate the total amount
    @Override
    public void calculateTotal() {
        totalAmount = products.stream()
                .mapToDouble(Product::getPrice)
                .sum(); // Sum all product prices
    }
}