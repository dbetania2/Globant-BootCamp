package com.shopi.shopping.models.products;
import com.shopi.shopping.models.ShoppingCart;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "products")  // Table name for all product types
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Product  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
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

    public Product(){

    }
    // Constructor to initialize product details
    protected Product(BigDecimal price, String name, String description) {
        this.price = price;
        this.name = name;
        this.description = description;
    }

    public void setId(long id) {
        this.id = id;
    }

    // Getters for product attributes
    public long getId() { return id; }
    public BigDecimal getPrice() { return price; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    // Setters for product attributes
    public void setPrice(BigDecimal price) { this.price = price; }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(price, product.price) &&
                Objects.equals(name, product.name) &&
                Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, name, description);
    }
}