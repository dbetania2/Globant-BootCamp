package com.shopi.shopping;

import com.shopi.shopping.factories.ProductFactory;
import com.shopi.shopping.models.products.Book;
import com.shopi.shopping.models.products.Clothing;
import com.shopi.shopping.models.products.Electronic;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.repositories.ProductRepository;
import com.shopi.shopping.services.ProductService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductFactory productFactory;

    @InjectMocks
    private ProductService productService;

    private Product electronicProduct;

    @BeforeEach
    public void setUp() {
        electronicProduct = new Electronic(BigDecimal.valueOf(699.99), "Smartphone", "Latest smartphone model");
        electronicProduct.setId(1L);
    }

    @Test
    public void testCreateProduct() {
        // Arrange
        when(productFactory.createProduct("ELECTRONIC", "Smartphone", BigDecimal.valueOf(699.99)))
                .thenReturn(electronicProduct);
        when(productRepository.save(any(Product.class))).thenReturn(electronicProduct); // Mocking the save method

        // Act
        Product createdProduct = productService.createProduct("ELECTRONIC", "Smartphone", BigDecimal.valueOf(699.99));

        // Assert
        assertNotNull(createdProduct);
        assertEquals("Smartphone", createdProduct.getName());
        verify(productRepository, times(1)).save(electronicProduct); // Asegúrate de que se guarda el producto
    }

    @Test
    public void testGetProductById() {
        // Arrange
        when(productRepository.findById(electronicProduct.getId())).thenReturn(Optional.of(electronicProduct));

        // Act
        Product fetchedProduct = productService.getProductById(electronicProduct.getId());

        // Assert
        assertNotNull(fetchedProduct);
        assertEquals(electronicProduct.getId(), fetchedProduct.getId());
        verify(productRepository, times(1)).findById(electronicProduct.getId());
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
    public void testDeleteProduct() {
        // Arrange
        long productId = electronicProduct.getId();
        when(productRepository.existsById(productId)).thenReturn(true);

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepository, times(1)).deleteById(productId);
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
    public void testDeleteNonExistingProduct() {
        // Arrange
        long productId = electronicProduct.getId();
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
        List<Product> productList = new ArrayList<>();
        productList.add(electronicProduct);
        when(productRepository.findAll()).thenReturn(productList);

        // Act
        List<Product> fetchedProducts = productService.getAllProducts();

        // Assert
        assertEquals(1, fetchedProducts.size());
        assertEquals(electronicProduct, fetchedProducts.get(0));
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductsByName() {
        // Arrange
        List<Product> productList = new ArrayList<>();
        productList.add(electronicProduct);
        when(productRepository.findByName("Smartphone")).thenReturn(productList);

        // Act
        List<Product> fetchedProducts = productService.getProductsByName("Smartphone");

        // Assert
        assertEquals(1, fetchedProducts.size());
        assertEquals(electronicProduct, fetchedProducts.get(0));
        verify(productRepository, times(1)).findByName("Smartphone");
    }

    @Test
    public void testGetProductsByCategory() {
        // Arrange
        List<Product> productList = new ArrayList<>();
        productList.add(electronicProduct);
        when(productRepository.findByCategory("Electronics")).thenReturn(productList);

        // Act
        List<Product> fetchedProducts = productService.getProductsByCategory("Electronics");

        // Assert
        assertEquals(1, fetchedProducts.size());
        assertEquals(electronicProduct, fetchedProducts.get(0));
        verify(productRepository, times(1)).findByCategory("Electronics");
    }
    //------------------

}