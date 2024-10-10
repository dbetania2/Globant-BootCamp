package com.shopi.shopping;

import com.shopi.shopping.factories.ProductFactory;
import com.shopi.shopping.models.products.Book;
import com.shopi.shopping.models.products.Clothing;
import com.shopi.shopping.models.products.Electronic;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.repositories.ProductRepository;
import com.shopi.shopping.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.testng.Assert.assertNotNull;

public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductFactory productFactory;

    @Mock
    private ProductRepository productRepository;

    private Product electronicProduct;
    private Product clothingProduct;
    private Product bookProduct;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        electronicProduct = new Electronic(BigDecimal.valueOf(699.99),"Smartphone", " good Smartphone" );
        clothingProduct = new Clothing(2L, "T-Shirt", BigDecimal.valueOf(19.99));
        bookProduct = new Book(3L, "Effective Java", BigDecimal.valueOf(39.99));
    }

    @Test
    public void testCreateProduct() {
        // Arrange
        String productType = "ELECTRONIC";
        String name = "Smartphone";
        BigDecimal price = BigDecimal.valueOf(699.99);
        when(productFactory.createProduct(productType, name, price)).thenReturn(electronicProduct);
        when(productRepository.save(any(Product.class))).thenReturn(electronicProduct);

        // Act
        Product createdProduct = productService.createProduct(productType, name, price);

        // Assert
        assertNotNull(createdProduct);
        assertEquals("Smartphone", createdProduct.getName());
        verify(productRepository, times(1)).save(electronicProduct);
    }

    @Test
    public void testGetProductById() {
        // Arrange
        long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(electronicProduct));

        // Act
        Product foundProduct = productService.getProductById(productId);

        // Assert
        assertNotNull(foundProduct);
        assertEquals(electronicProduct, foundProduct);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void testUpdateProduct() {
        // Arrange
        when(productRepository.existsById(electronicProduct.getId())).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(electronicProduct);

        // Act
        productService.updateProduct(electronicProduct);

        // Assert
        verify(productRepository, times(1)).save(electronicProduct);
    }

    @Test
    public void testUpdateNonExistingProduct() {
        // Arrange
        when(productRepository.existsById(electronicProduct.getId())).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(electronicProduct);
        });
        assertEquals("Product does not exist", exception.getMessage());
    }

    @Test
    public void testDeleteProduct() {
        // Arrange
        long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(true);

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void testDeleteNonExistingProduct() {
        // Arrange
        long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.deleteProduct(productId);
        });
        assertEquals("Product does not exist", exception.getMessage());
    }

    @Test
    public void testGetAllProducts() {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(electronicProduct);
        products.add(clothingProduct);
        products.add(bookProduct);
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> foundProducts = productService.getAllProducts();

        // Assert
        assertEquals(3, foundProducts.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductsByName() {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(electronicProduct);
        when(productRepository.findByName("Smartphone")).thenReturn(products);

        // Act
        List<Product> foundProducts = productService.getProductsByName("Smartphone");

        // Assert
        assertEquals(1, foundProducts.size());
        assertEquals(electronicProduct, foundProducts.get(0));
        verify(productRepository, times(1)).findByName("Smartphone");
    }

    @Test
    public void testGetProductsByCategory() {
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(clothingProduct);
        when(productRepository.findByCategory("CLOTHING")).thenReturn(products);

        // Act
        List<Product> foundProducts = productService.getProductsByCategory("CLOTHING");

        // Assert
        assertEquals(1, foundProducts.size());
        assertEquals(clothingProduct, foundProducts.get(0));
        verify(productRepository, times(1)).findByCategory("CLOTHING");
    }
}