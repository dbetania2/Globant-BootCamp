<<<<<<< HEAD:src/models/StandardOrder.java
package models;

import models.products.Product;
=======
package com.shopi.shopping.models;

import com.shopi.shopping.models.products.Product;
>>>>>>> topic2formerge:src/main/java/com/shopi/shopping/models/StandardOrder.java

import java.util.List;

public class StandardOrder extends Order {

    public StandardOrder(List<Product> products) {
        super(products);
    }

    // Calculate total price for all products in the order
    @Override
    public void calculateTotal() {
        double total = products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
        setTotalAmount(total);
    }
<<<<<<< HEAD:src/models/StandardOrder.java
}

=======
}
>>>>>>> topic2formerge:src/main/java/com/shopi/shopping/models/StandardOrder.java
