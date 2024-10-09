package com.shopi.shopping.models.products;

import com.shopi.shopping.models.ShoppingCart;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)  // Strategy for inheritance
public abstract class Product  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double price;
    private String name;
    private String description;

    // Many Products can be in Many ShoppingCarts
    @ManyToMany
    @JoinTable(
            name = "shopping_cart_products",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "shopping_cart_id")
    )
    private List<ShoppingCart> shoppingCarts = new ArrayList<>(); // Updated to match the relationship

    // Constructor to initialize product details
    protected Product(double price, String name, String description) {
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

    // Abstract method to get the product type
    public abstract String getType();

    // Override toString for a better representation of the product
    @Override
    public String toString() {
        return String.format("%-10s | %-15s | %-20s | %.2f",
                id, name, description, price);
    }
}