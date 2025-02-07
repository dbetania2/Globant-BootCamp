package com.shopi.shopping.factories;
import  com.shopi.shopping.models.products.*;  // Import all product classes
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


// Factory class for creating different types of products

@Component
public class ProductFactory {
    private static final Logger logger = LoggerFactory.getLogger(ProductFactory.class);

    // Method to create a product based on type
    public Product createProduct(String productType, String name, BigDecimal price) {
        // Checks to avoid null parameters
        if (productType == null || name == null || price == null) {
            logger.error("Failed to create product: Product type, name, or price cannot be null");
            throw new IllegalArgumentException("Product type, name, and price cannot be null");
        }

        logger.info("Creating product of type: {}, name: {}, price: {}", productType, name, price);

        switch (productType.toUpperCase()) {
            case "ELECTRONIC":
                return new Electronic(price, name, "Default description"); // Using a default description
            case "CLOTHING":
                return new Clothing(price, name, "Default description"); // Using a default description
            case "BOOK":
                return new Book(price, name, "Default description"); // Using a default description
            default:
                logger.error("Failed to create product: Unknown product type: {}", productType);
                throw new IllegalArgumentException("Unknown product type: " + productType);
        }
    }
}
