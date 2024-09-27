package main.java.com.shopi.shopping.models;

import main.java.com.shopi.shopping.models.products.Product;



import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    private static long cartIdCounter = 120300;  // Starts at 120300 as an example
    private long id;
    private Customer customer;
    private List<Product> products;
    private Status status;

    public enum Status {
        DRAFT, SUBMIT
    }

    // Constructor
    public ShoppingCart(Customer customer) {
        this.id = generateCartId();  // Generates a unique ID for the cart
        this.products = new ArrayList<>();  // Initialize empty product list
        this.status = Status.DRAFT;  // Default status is DRAFT
        this.customer = customer;  // Assign customer to the cart
    }

    // Method to generate a unique ID
    private synchronized long generateCartId() {
        return cartIdCounter++;  // Returns current value, then increments
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Print all cart information
    @Override
    public String toString() {
        StringBuilder printCart = new StringBuilder();

        // Table headers
        printCart.append(String.format("%-10s | %-3s | %-15s | %-20s | %-5s\n", "Cart ID", "ID", "PRODUCT TYPE", "PRODUCT NAME", "PRICE"));
        printCart.append("-----------------------------------------------------------------------\n");

        // Display products from the list
        for (Product product : products) {
            printCart.append(String.format("%-10s | %-3s | %-15s | %-20s | %.2f\n",
                    id,                 // Cart ID
                    product.getId(),    // Product ID
                    product.getType(),  // Product Type
                    product.getName(),  // Product Name
                    product.getPrice())); // Price
        }

        return printCart.toString();
    }
}

