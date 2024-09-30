package factories;

import models.Order;
import models.StandardOrder;
import models.products.Product;

import java.util.List;

/**
 * Factory class for creating different types of orders.
 *
 */
import java.util.List;

import java.util.List;

public class OrderFactory {

    // Create a standard order with the provided products
    public Order createOrder(List<Product> products) {
        return new StandardOrder(products); // Automatically calculates total in the Order constructor
    }
}
