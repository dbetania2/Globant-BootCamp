package com.shopi.shopping.models;
import com.shopi.shopping.models.products.Product;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;



@Entity
@Table(name = "orders")
public abstract class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToMany
    @JoinTable(
            name = "order_discounts",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id")
    )
    protected List<Discount> appliedDiscounts = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    protected List<Product> products;

    protected BigDecimal totalAmount = BigDecimal.ZERO; // Use BigDecimal for total amount precision

    private static final AtomicLong idCounter = new AtomicLong(0);

    // No-argument constructor
    public Order() {}

    // Constructor to initialize with products
    public Order(List<Product> products) {
        this.products = products;
        this.id = idCounter.incrementAndGet();
        calculateTotal();  // Calculate the total amount when creating the order
    }

    // Getters and setters for order properties
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAppliedDiscounts(List<Discount> appliedDiscounts) {
        this.appliedDiscounts = appliedDiscounts;
        calculateTotal();  // Recalculate total when setting new discounts
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        calculateTotal();  // Recalculate total when setting new products
    }

    public List<Product> getProducts() {
        return products;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public List<Discount> getAppliedDiscounts() {
        return appliedDiscounts;
    }

    // Method to calculate the total amount based on the products and discounts
    public void calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;

        // Log the start of the calculation
        System.out.println("Calculando total para los productos...");

        for (Product product : products) {
            total = total.add(product.getPrice());
            // Log each product price
            System.out.println("Añadiendo precio del producto: " + product.getName() + " - Precio: " + product.getPrice());
        }

        // Log the total calculated before updating the order total
        System.out.println("Total calculado antes de establecer: " + total);

        setTotalAmount(total);

        // Log the total set
        System.out.println("Total establecido en la orden: " + getTotalAmount());
    }


    // Override the toString method to provide a readable representation of the order
    @Override
    public String toString() {
        return "Order ID: " + id + ", Total Amount: " + totalAmount + ", Discounts Applied: " + appliedDiscounts.size();
    }

    // Optional implementation of equals and hashCode methods for entity equality
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Method to set the total amount directly
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    // Method to check if a discount has already been applied
    public boolean hasDiscount(Discount discount) {
        return appliedDiscounts.contains(discount);
    }
}