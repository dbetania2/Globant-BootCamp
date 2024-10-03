package com.shopi.shopping;
import com.shopi.shopping.factories.OrderFactory;
import com.shopi.shopping.factories.ProductFactory;
import com.shopi.shopping.models.Customer;
import com.shopi.shopping.models.Order;
import com.shopi.shopping.models.ShoppingCart;
import com.shopi.shopping.models.StandardOrder;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.services.DiscountService;
import com.shopi.shopping.services.ShoppingCartServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;


public class ShoppingCartServicesTest {

    private ShoppingCartServices shoppingCartServices;
    private OrderFactory orderFactory;
    private DiscountService discountService;
    private ShoppingCart cart;

    @BeforeEach
    public void setUp() {
        // Create mocks for the dependencies
        orderFactory = Mockito.mock(OrderFactory.class);
        discountService = Mockito.mock(DiscountService.class);

        // Create an instance of the service with the mocked dependencies
        shoppingCartServices = new ShoppingCartServices(orderFactory, discountService);

        // Create a customer using the CustomerBuilder
        Customer customer = new Customer.CustomerBuilder("John", "Smith")
                .setBirthDate(LocalDate.of(1990, 1, 1))
                .setEmail("john@gmail.com")
                .setPhone("123456789")
                .setIdentificationNumber("DNI123")
                .build();

        // Simulate a shopping cart with the customer
        cart = new ShoppingCart(customer);

        // Create concrete products using ProductFactory
        Product book = ProductFactory.createProduct("BOOK", "A novel", 50.0);
        Product phone = ProductFactory.createProduct("ELECTRONICS", "Smartphone", 300.0);

        // Add products to the cart using the service
        shoppingCartServices.addProductToCart(cart, book);
        shoppingCartServices.addProductToCart(cart, phone);
    }

    @Test
    public void testCheckout() {
        // Create a simulated order
        Order order = new StandardOrder(cart.getProducts());
        when(orderFactory.createOrder(cart.getProducts())).thenReturn(order);

        // Execute the checkout method
        Order resultOrder = shoppingCartServices.checkout(cart, true);

        // Verify that an order was created and the discount was applied
        verify(orderFactory).createOrder(cart.getProducts());
        verify(discountService).applyFirstPurchaseDiscount(order, true); // true indicates its the first purchase

        // Verify that the returned order is not null
        assertNotNull(resultOrder);
    }


    @Test
    public void testCalculateTotalPrice() {
        // Execute the total price calculation
        double totalPrice = shoppingCartServices.calculateTotalPrice(cart);

        // Verify that the total price is as expected
        assertEquals(350.0, totalPrice);
    }

    @Test
    public void testAddProductToCart() {
        Product newProduct = ProductFactory.createProduct("BOOK", "Gaming laptop", 150.0);

        // Add the new product to the cart
        shoppingCartServices.addProductToCart(cart, newProduct);

        // Verify that the product was added to the cart
        assertTrue(cart.getProducts().contains(newProduct));
    }

    @Test
    public void testViewCart() {
        // Simulate the console output
        shoppingCartServices.viewCart(cart);

        // Cannot verify console output at this level,check if the method executed without exceptions
        assertDoesNotThrow(() -> shoppingCartServices.viewCart(cart));
    }
}


