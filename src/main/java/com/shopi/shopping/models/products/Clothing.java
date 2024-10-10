package com.shopi.shopping.models.products;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "clothing")  // Maps this class to the "clothing" table in the database
public class Clothing extends Product {

    // Constructor to initialize the clothing product
    public Clothing(BigDecimal price, String name, String description) {
        super(price, name, description);
    }

    @Override
    public String getType() {
        return "CLOTHING";  // Returns the type of product
    }
}