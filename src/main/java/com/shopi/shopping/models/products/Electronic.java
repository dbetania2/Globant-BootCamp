package com.shopi.shopping.models.products;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;


@Entity
@DiscriminatorValue("ELECTRONIC")  // Specifies the discriminator value for this subclass
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