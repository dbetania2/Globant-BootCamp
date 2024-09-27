package com.shopi.shopping.factories;
import src.-main.java.com.shopi.shopping.models.products.*;  // Import all product classes


// Factory class for creating different types of products

public class ProductFactory {
    public static Product createProduct(String productType, String name, double price) {
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
                throw new IllegalArgumentException("Unknown product type");
        }
    }
}
