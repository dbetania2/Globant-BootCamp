package com.shopi.shopping.models.products;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("BOOK")  // Specifies the discriminator value for this subclass
public class Book extends Product {

    public Book() {
        super();
    }

    // Constructor to initialize the book product
    public Book(BigDecimal price, String name, String description) {
        super(price, name, description);
    }

    @Override
    public String getType() {
        return "BOOK";  // Returns the type of product
    }
}