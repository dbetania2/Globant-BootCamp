package com.shopi.shopping.factories;

import com.shopi.shopping.models.Order;
import com.shopi.shopping.models.StandardOrder;
import com.shopi.shopping.models.products.Product;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Factory class for creating different types of orders.
 *
 */
import java.util.List;

import java.util.List;
@Component
public class OrderFactory {

    // Create a standard order with the provided products
    public Order createOrder(List<Product> products) {
        return new StandardOrder(products); // Automatically calculates total in the Order constructor
    }
}