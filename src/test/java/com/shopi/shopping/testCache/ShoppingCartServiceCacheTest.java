package com.shopi.shopping.testCache;
import com.shopi.shopping.models.ShoppingCart;
import com.shopi.shopping.repositories.ShoppingCartRepository;
import com.shopi.shopping.services.ShoppingCartServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingCartServiceCacheTest {

    @Autowired
    private ShoppingCartServices shoppingCartService; // Autowired to load the service into the Spring context

    @MockBean
    private ShoppingCartRepository shoppingCartRepository; // MockBean for the repository

    @Test
    void testGetCartsByStatusWithCache_UsesCache() {
        // Setup
        ShoppingCart.Status status = ShoppingCart.Status.DRAFT;

        // Create a mock list of carts with the specified status
        List<ShoppingCart> mockCarts = new ArrayList<>();
        ShoppingCart mockCart1 = new ShoppingCart();
        mockCart1.setId(1L);
        mockCarts.add(mockCart1);

        // Simulate that the carts are not in the cache and are retrieved from the repository
        when(shoppingCartRepository.findByStatus(status)).thenReturn(mockCarts);

        // Call the method - First call should access the repository
        List<ShoppingCart> cartsFromCache = shoppingCartService.getCartsByStatusWithCache(status);

        // Verify that the carts were obtained from the repository
        assertNotNull(cartsFromCache);
        assertEquals(1, cartsFromCache.size());
        assertEquals(mockCart1, cartsFromCache.get(0));

        // Call the method - Second call should use the cache
        List<ShoppingCart> cartsFromCacheAgain = shoppingCartService.getCartsByStatusWithCache(status);

        // Verify that the repository was not called this second time
        verify(shoppingCartRepository, times(1)).findByStatus(status); // Should only be one call
        assertEquals(cartsFromCache, cartsFromCacheAgain);
    }

    @Test
    void testDeleteCartByIdWithCacheEviction() {
        // Setup
        long cartId = 2L;

        // Simulate that the cart exists
        when(shoppingCartRepository.existsById(cartId)).thenReturn(true);

        // Call the method to delete the cart with cache eviction
        shoppingCartService.deleteCartByIdWithCache(cartId);

        // Verify that the delete method of the repository was called
        verify(shoppingCartRepository, times(1)).deleteById(cartId);

        // Verify if the cache was invalidated (optionally, you can check if the cacheManager invalidates it)
    }

    @Test
    void testGetCartByIdWithCache_UsesCache() {
        // Setup
        long cartId = 1L;

        // Create a test shopping cart
        ShoppingCart mockCart = new ShoppingCart();
        mockCart.setId(cartId);

        // Simulate that the cart is not in the cache and is retrieved from the repository
        when(shoppingCartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));

        // Call the method - First time, should get the cart from the repository
        Optional<ShoppingCart> cartFromCache = shoppingCartService.getCartByIdWithCache(cartId);

        // Verify that the cart was obtained from the repository
        assertTrue(cartFromCache.isPresent());
        assertEquals(mockCart, cartFromCache.get());

        // Call the method - Second time, should use the cache
        Optional<ShoppingCart> cartFromCacheAgain = shoppingCartService.getCartByIdWithCache(cartId);

        // Verify that the repository was not called the second time
        verify(shoppingCartRepository, times(1)).findById(cartId); // Should only be one call
        assertEquals(mockCart, cartFromCacheAgain.get());
    }
}
