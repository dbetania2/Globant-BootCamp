package com.shopi.shopping;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.shopi.shopping.factories.ProductFactory;
import com.shopi.shopping.models.products.Book;
import com.shopi.shopping.models.products.Clothing;
import com.shopi.shopping.models.products.Electronic;
import com.shopi.shopping.models.products.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

public class ProductFactoryTest {

    @InjectMocks
    private ProductFactory productFactory;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testCreateElectronicProduct() {
        // Arrange
        String productType = "ELECTRONIC";
        String name = "Smartphone";
        BigDecimal price = new BigDecimal("699.99");

        // Act
        Product product = productFactory.createProduct(productType, name, price);

        // Assert
        assertNotNull(product);
        assertTrue(product instanceof Electronic);
        assertEquals(name, product.getName());
        assertEquals(price, product.getPrice());
    }

    @Test
    public void testCreateClothingProduct() {
        // Arrange
        String productType = "CLOTHING";
        String name = "T-Shirt";
        BigDecimal price = new BigDecimal("19.99");

        // Act
        Product product = productFactory.createProduct(productType, name, price);

        // Assert
        assertNotNull(product);
        assertTrue(product instanceof Clothing);
        assertEquals(name, product.getName());
        assertEquals(price, product.getPrice());
    }

    @Test
    public void testCreateBookProduct() {
        // Arrange
        String productType = "BOOK";
        String name = "Effective Java";
        BigDecimal price = new BigDecimal("45.00");

        // Act
        Product product = productFactory.createProduct(productType, name, price);

        // Assert
        assertNotNull(product);
        assertTrue(product instanceof Book);
        assertEquals(name, product.getName());
        assertEquals(price, product.getPrice());
    }

    @Test
    public void testCreateProductWithNullType() {
        // Arrange
        String productType = null;
        String name = "Sample Product";
        BigDecimal price = new BigDecimal("20.00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productFactory.createProduct(productType, name, price)
        );
        assertEquals("Product type, name, and price cannot be null", exception.getMessage());
    }

    @Test
    public void testCreateProductWithNullName() {
        // Arrange
        String productType = "ELECTRONIC";
        String name = null;
        BigDecimal price = new BigDecimal("20.00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productFactory.createProduct(productType, name, price)
        );
        assertEquals("Product type, name, and price cannot be null", exception.getMessage());
    }

    @Test
    public void testCreateProductWithNullPrice() {
        // Arrange
        String productType = "CLOTHING";
        String name = "Shirt";
        BigDecimal price = null;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productFactory.createProduct(productType, name, price)
        );
        assertEquals("Product type, name, and price cannot be null", exception.getMessage());
    }

    @Test
    public void testCreateProductWithUnknownType() {
        // Arrange
        String productType = "UNKNOWN";
        String name = "Unknown Product";
        BigDecimal price = new BigDecimal("100.00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                productFactory.createProduct(productType, name, price)
        );
        assertEquals("Unknown product type: UNKNOWN", exception.getMessage());
    }
}
