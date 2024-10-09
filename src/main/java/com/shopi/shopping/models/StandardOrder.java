package com.shopi.shopping.models;
import com.shopi.shopping.models.products.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;


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
}
