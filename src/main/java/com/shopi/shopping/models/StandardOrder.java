
package com.shopi.shopping.models;

import com.shopi.shopping.models.products.Product;

import java.util.List;

public class StandardOrder extends Order {
    public StandardOrder(List<Product> products) {
        super(products);
    }

    @Override
    public void calculateTotal() {
        totalAmount = products.stream()
                .mapToDouble(Product::getPrice)
                .sum(); // Sum all product prices
    }
}

