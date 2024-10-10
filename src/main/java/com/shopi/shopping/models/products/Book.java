package com.shopi.shopping.models.products;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "books")  // Maps this class to the "books" table in the database
public class Book extends Product {

    // Constructor to initialize the book product
    public Book(BigDecimal price, String name, String description) {
        super(price, name, description);
    }

    @Override
    public String getType() {
        return "BOOK";  // Returns the type of product
    }
}