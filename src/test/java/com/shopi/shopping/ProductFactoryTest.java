package com.shopi.shopping;

import com.shopi.shopping.factories.ProductFactory;
import com.shopi.shopping.models.products.Book;
import com.shopi.shopping.models.products.Clothing;
import com.shopi.shopping.models.products.Electronic;
import com.shopi.shopping.models.products.Product;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.testng.Assert.assertNotNull;

public class ProductFactoryTest {

    @Test
    public void testCreateElectronicsProduct() {
        // Arrange
        String productType = "ELECTRONICS";
        String name = "Smartphone";
        double price = 699.99;

        // Act
        Product product = ProductFactory.createProduct(productType, name, price);

        // Assert
        assertNotNull(product, "Product should not be null");
        assertTrue(product instanceof Electronic, "Product should be an instance of Electronic");
        assertEquals("Smartphone", product.getName(), "Product name should match");
        assertEquals(699.99, product.getPrice(), 0.001, "Product price should match");
    }

    @Test
    public void testCreateClothingProduct() {
        // Arrange
        String productType = "CLOTHING";
        String name = "T-Shirt";
        double price = 19.99;

        // Act
        Product product = ProductFactory.createProduct(productType, name, price);

        // Assert
        assertNotNull(product, "Product should not be null");
        assertTrue(product instanceof Clothing, "Product should be an instance of Clothing");
        assertEquals("T-Shirt", product.getName(), "Product name should match");
        assertEquals(19.99, product.getPrice(), 0.001, "Product price should match");
    }

    @Test
    public void testCreateBookProduct() {
        // Arrange
        String productType = "BOOK";
        String name = "Effective Java";
        double price = 39.99;

        // Act
        Product product = ProductFactory.createProduct(productType, name, price);

        // Assert
        assertNotNull(product, "Product should not be null");
        assertTrue(product instanceof Book, "Product should be an instance of Book");
        assertEquals("Effective Java", product.getName(), "Product name should match");
        assertEquals(39.99, product.getPrice(), 0.001, "Product price should match");
    }

    @Test
    public void testCreateUnknownProductType() {
        // Arrange
        String productType = "UNKNOWN";
        String name = "Unknown Product";
        double price = 0.0;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ProductFactory.createProduct(productType, name, price);
        });
        assertEquals("Unknown product type", exception.getMessage());
    }
}