package com.shopi.shopping;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.shopi.shopping.controllers.ShoppingCartController;
import com.shopi.shopping.models.ShoppingCart;
import com.shopi.shopping.repositories.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ShoppingCartControllerTest {

    @InjectMocks
    private ShoppingCartController shoppingCartController;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrUpdateCart() {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        // Set other properties of the cart as needed

        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(cart);

        ResponseEntity<ShoppingCart> response = shoppingCartController.createOrUpdateCart(cart);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    public void testGetAllCarts() {
        ShoppingCart cart1 = new ShoppingCart();
        ShoppingCart cart2 = new ShoppingCart();
        List<ShoppingCart> carts = Arrays.asList(cart1, cart2);

        when(shoppingCartRepository.findAll()).thenReturn(carts);

        ResponseEntity<List<ShoppingCart>> response = shoppingCartController.getAllCarts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carts, response.getBody());
    }

    @Test
    public void testGetCartById_Found() {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        when(shoppingCartRepository.findById(1L)).thenReturn(Optional.of(cart));

        ResponseEntity<ShoppingCart> response = shoppingCartController.getCartById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    public void testGetCartById_NotFound() {
        when(shoppingCartRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<ShoppingCart> response = shoppingCartController.getCartById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteCartById_Found() {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        when(shoppingCartRepository.findById(1L)).thenReturn(Optional.of(cart));

        ResponseEntity<Void> response = shoppingCartController.deleteCartById(1L);

        verify(shoppingCartRepository, times(1)).deleteById(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteCartById_NotFound() {
        when(shoppingCartRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = shoppingCartController.deleteCartById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(shoppingCartRepository, never()).deleteById(any(Long.class));
    }

    @Test
    public void testGetCartsByCustomerId() {
        ShoppingCart cart = new ShoppingCart();
        List<ShoppingCart> carts = Arrays.asList(cart);

        when(shoppingCartRepository.findByCustomerId(1L)).thenReturn(carts);

        ResponseEntity<List<ShoppingCart>> response = shoppingCartController.getCartsByCustomerId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carts, response.getBody());
    }

    @Test
    public void testGetCartsByStatus() {
        ShoppingCart cart = new ShoppingCart();
        List<ShoppingCart> carts = Arrays.asList(cart);

        when(shoppingCartRepository.findByStatus(any(ShoppingCart.Status.class))).thenReturn(carts);

        ResponseEntity<List<ShoppingCart>> response = shoppingCartController.getCartsByStatus(ShoppingCart.Status.DRAFT);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carts, response.getBody());
    }
}
