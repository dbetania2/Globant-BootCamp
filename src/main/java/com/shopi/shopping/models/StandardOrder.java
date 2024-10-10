package com.shopi.shopping.models;
import com.shopi.shopping.models.products.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "standard_orders") // Optional: Specify the table name
public class StandardOrder extends Order {

    // No-argument constructor for JPA
    public StandardOrder() {
        super(); // Call the no-argument constructor of the parent class
    }

    // Constructor to initialize the order with products
    public StandardOrder(List<Product> products) {
        super(products); // Call the parent constructor to initialize products

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StandardOrder)) return false;
        StandardOrder that = (StandardOrder) o;
        return Objects.equals(products, that.products) &&
                true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(products);
    }

}
