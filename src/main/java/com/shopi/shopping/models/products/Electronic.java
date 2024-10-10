package com.shopi.shopping.models.products;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;


@Entity
@Table(name = "electronics")  // Maps this class to the "electronics" table in the database
public class Electronic extends Product {

    // Constructor to initialize the electronic product
    public Electronic(BigDecimal price, String name, String description) {
        super(price, name, description);
    }

    @Override
    public String getType() {
        return "ELECTRONIC";  // Returns the type of product
    }
}