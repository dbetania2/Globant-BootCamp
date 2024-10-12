package com.shopi.shopping.models;
import com.shopi.shopping.models.products.Product;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Table(name = "shopping_carts")  // Maps the class to the "shopping_carts" table in the database
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Automatically generates the cart ID
    private long id;

    @ManyToOne  // Relationship with Customer
    @JoinColumn(name = "customer_id")  // Foreign key column in the shopping_carts table
    private Customer customer;

    @ManyToMany(mappedBy = "shoppingCarts")
    private List<Product> products = new ArrayList<>();


    @Enumerated(EnumType.STRING)  // Store enum as String in the database
    private Status status;

    public enum Status {
        DRAFT, SUBMIT
    }

    private static final AtomicLong idCounter = new AtomicLong(0);
    public ShoppingCart() {
        this.products = new ArrayList<>();
    }
    // Constructor
    public ShoppingCart(Customer customer) {
        this.customer = customer;  // Assign customer to the cart
        this.status = Status.DRAFT;  // Default status is DRAFT
        this.id = idCounter.incrementAndGet();
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

    public void setId(long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ShoppingCart that = (ShoppingCart) obj;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}