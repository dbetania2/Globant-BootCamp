package com.shopi.shopping.testCache;
import com.shopi.shopping.factories.ProductFactory;
import com.shopi.shopping.models.products.Electronic;
import com.shopi.shopping.models.products.Book;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.repositories.ProductRepository;
import com.shopi.shopping.services.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;

import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;



@SpringBootTest
@AutoConfigureMockMvc
@EnableCaching
public class ProductServiceCacheTest {

    @Autowired
    private ProductService productService; // Autowired to load the service with the Spring context

    @MockBean
    private ProductRepository productRepository; // MockBean for the repository

    @Autowired
    private CacheManager cacheManager; // Autowired to access the cache manager

    @AfterEach
    public void clearCache() {
        // Limpia el caché después de cada prueba
        cacheManager.getCache("products").clear();
    }

    @Test
    public void testGetProductByIdCached() {
        // Given
        long productId = 1L;
        Book book = new Book(new BigDecimal("19.99"), "Test Book", "A test book description");
        when(productRepository.findById(productId)).thenReturn(Optional.of(book)); // Mock repository

        // When - First call should hit the repository
        Product result1 = productService.getProductByIdCached(productId);
        verify(productRepository, times(1)).findById(productId); // Verify repository is called once

        // When - Second call should come from cache, repository shouldn't be called
        Product result2 = productService.getProductByIdCached(productId);
        verify(productRepository, times(1)).findById(productId); // Ensure repository is not called again

        // Then - Assert the result
        assertEquals(book, result1);
        assertEquals(book, result2);
    }

    @Test
    void testDeleteProductWithoutCacheEviction() {
        // Setup
        long productId = 1L;
        // Simulate that the product exists
        when(productRepository.existsById(productId)).thenReturn(true);

        // Call the method to delete the product
        productService.deleteProduct(productId);

        // Verify that the repository's delete method was called
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testDeleteProductWithCacheEviction() {
        // Setup
        long productId = 2L;
        // Simulate that the product exists
        when(productRepository.existsById(productId)).thenReturn(true);

        // Call the method to delete the product with cache eviction
        productService.deleteProductWithCache(productId);

        // Verify that the repository's delete method was called
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testUpdateProductWithoutCacheEviction() {
        // Setup
        long productId = 1L;

        // Create a test product
        Electronic mockProduct = new Electronic(BigDecimal.valueOf(10.00), "Updated Electronic Product", "An updated description for the test product.");
        mockProduct.setId(productId); // Set the ID in the mock product

        // Simulate that the product exists in the repository
        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.save(mockProduct)).thenReturn(mockProduct); // Simulate save behavior

        // Call the method to update the product
        productService.updateProduct(mockProduct);

        // Verify that the save method was called exactly once
        verify(productRepository, times(1)).save(mockProduct);

        // Verify that the cache was not evicted
    }

    @Test
    void testUpdateProductWithCacheEviction() {
        // Setup
        long productId = 1L;
        Product existingProduct = new Electronic(BigDecimal.valueOf(10.00), "Test Electronic", "A test product.");
        existingProduct.setId(productId); // Assign the ID to the mock product

        // Simulate that the product exists and is returned
        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.save(existingProduct)).thenReturn(existingProduct); // Simulate save behavior
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct)); // Simulate finding by ID

        // Call the method to get the product, this should go to the cache
        Product cachedProduct = productService.getProductByIdCached(productId); // Call the real method that uses cache

        // Verify that the product was obtained from the cache
        assertNotNull(cachedProduct);
        assertEquals(existingProduct, cachedProduct);

        // Call the update method with cache eviction
        productService.updateProductWithCache(existingProduct);

        // Verify that the save method was called
        verify(productRepository, times(1)).save(existingProduct);

        // After the update, try to get the product again
        Product updatedProduct = productService.getProductByIdCached(productId); // Should go to the repository now

        // Verify that the repository was called to get the updated product
        verify(productRepository, times(2)).findById(productId); // Should have been called twice: before and after the update
        assertEquals(existingProduct, updatedProduct);
    }
}