package models;

import models.products.Product;

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
}

