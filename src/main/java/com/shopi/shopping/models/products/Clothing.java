package com.shopi.shopping.models.products;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


import java.math.BigDecimal;

@Entity
@DiscriminatorValue("CLOTHING")  // Specifies the discriminator value for this subclass
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