package com.shopi.shopping.testCache;
import com.shopi.shopping.factories.ProductFactory;
import com.shopi.shopping.models.products.Electronic;
import com.shopi.shopping.models.products.Book;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.repositories.ProductRepository;
import com.shopi.shopping.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductServiceCacheTest {

    @Autowired
    private ProductService productService; // Autowired to load the service with the Spring context

    @MockBean
    private ProductRepository productRepository; // MockBean for the repository

    /*@Test
    void testGetProductByIdCached_UsesCache() {
        // Setup
        long productId = 1L;

        // Create a product of type Electronic and set its ID
        Electronic mockProduct = new Electronic(BigDecimal.valueOf(10.00), "Test Electronic Product", "A description for the test product.");
        mockProduct.setId(productId); // Ensure that the ID matches

        // Simulate that the product is not in cache and is fetched from the repository
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        // Call the method - First call, should access the repository
        Product productFromCache = productService.getProductByIdCached(productId);

        // Verify that the product was obtained from the repository
        assertNotNull(productFromCache);
        assertEquals(mockProduct, productFromCache);

        // Call the method - Second call, should use cache
        Product productFromCacheAgain = productService.getProductByIdCached(productId);

        // Verify that the repository is not called this time
        verify(productRepository, times(1)).findById(productId); // Should only be one call
        assertEquals(mockProduct, productFromCacheAgain);
    }*/

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

        // Verify that the cache was not evicted (this can be done by ensuring the cache method was not called)
        // In this case, just ensure that your method logic does not trigger cache eviction.
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
