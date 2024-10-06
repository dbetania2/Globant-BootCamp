package com.shopi.shopping.factories;
import  com.shopi.shopping.models.products.*;  // Import all product classes
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


// Factory class for creating different types of products
@Component
public class ProductFactory {
    private static final Logger logger = LoggerFactory.getLogger(ProductFactory.class);

    public static Product createProduct(String productType, String name, double price) {
        logger.info("Creating product of type: {}, name: {}, price: {}", productType, name, price);    //logger------------
        switch (productType.toUpperCase()) {
            case "ELECTRONICS":
                // Create an Electronics product with a default description
                return new Electronic(price, name, "Default description");
            case "CLOTHING":
                // Create a Clothing product with a default description
                return new Clothing(price, name, "Default description");
            case "BOOK":
                // Create a Book product with a default description
                return new Book(price, name, "Default description");
            default:
                // Log the error before throwing the exception
                logger.error("Failed to create product: Unknown product type: {}", productType);    //logger------------
                throw new IllegalArgumentException("Unknown product type");
        }
    }
}