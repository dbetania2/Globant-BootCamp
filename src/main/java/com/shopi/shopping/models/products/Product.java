package main.java.com.shopi.shopping.models.products;

public abstract class Product {
    private static long productIdCounter = 1;  // Static counter for unique product IDs
    private long id;  // Unique ID for each product
    private double price;
    private String name;
    private String description;

    // Constructor to initialize product details
    public Product(double price, String name, String description) {
        this.id = generateProductId();  // Generates a unique ID for the product as an example
        this.price = price;
        this.name = name;
        this.description = description;
    }

    // Getters for product attributes
    public long getId() { return id; }
    public double getPrice() { return price; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    // Setters for product attributes
    public void setPrice(double price) { this.price = price; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }

    // Method to generate a unique ID
    private synchronized long generateProductId() {
        return productIdCounter++;  // Increments the counter for next ID
    }

    // Abstract method to get the product type
    public abstract String getType();

    // Override toString for a better representation of the product
    @Override
    public String toString() {
        return String.format("%-10s | %-15s | %-20s | %.2f",
                id, name, description, price);
    }
}