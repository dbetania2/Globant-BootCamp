package com.shopi.shopping.models;
import com.shopi.shopping.models.products.Product;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    protected double totalAmount;

    public Order() {
        // No-argument constructor
    }

    public Order(List<Product> products) {
        this.products = products;
        calculateTotal();  // Calculate total when creating the order
    }

    // Getters and Setters
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

    public double getTotalAmount() {
        return totalAmount;
    }

    public List<Discount> getAppliedDiscounts() {
        return appliedDiscounts;
    }

    // Method to calculate the total of the order considering products and discounts
    public void calculateTotal() {
        totalAmount = products.stream()
                .mapToDouble(Product::getPrice)
                .sum();

        totalAmount = Math.max(totalAmount, 0);
    }

    // Method to add a discount if it hasn't been applied before
    public void addDiscount(Discount discount) {
        // Check if the discount is not null and hasn't been applied before
        if (discount != null && !appliedDiscounts.contains(discount)) {
            // Add the discount to the list of applied discounts
            appliedDiscounts.add(discount);

            // Calculate the new total of the order
            calculateTotal(); // Update the total of the order after adding the discount
        }
    }

    @Override
    public String toString() {
        return "Order ID: " + id + ", Total Amount: " + totalAmount + ", Discounts Applied: " + appliedDiscounts.size();
    }

    // Optional implementation of equals and hashCode if needed
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

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount; // Set the value directly
    }

    // Method to check if the discount has already been applied
    public boolean hasDiscount(Discount discount) {
        return appliedDiscounts.contains(discount); // Return true if the discount is already in the list
    }
}
