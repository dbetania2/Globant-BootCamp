package com.shopi.shopping;
import java.util.*;
import com.shopi.shopping.factories.OrderFactory;
import com.shopi.shopping.factories.ProductFactory;
import com.shopi.shopping.models.Customer;
import com.shopi.shopping.models.Order;
import com.shopi.shopping.models.ShoppingCart;
import com.shopi.shopping.models.StandardOrder;
import com.shopi.shopping.models.products.Book;
import com.shopi.shopping.models.products.Electronic;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.services.DiscountService;
import com.shopi.shopping.services.ShoppingCartServices;
import com.shopi.shopping.repositories.ShoppingCartRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.io.ByteArrayOutputStream;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServicesTest {

    @InjectMocks
    private ShoppingCartServices shoppingCartServices; // Class we are testing

    @Mock
    private ShoppingCartRepository shoppingCartRepository; // Mock for the repository

    @Mock
    private OrderFactory orderFactory; // Mock for the order factory

    @Mock
    private DiscountService discountService; // Mock for the discount service

    @Mock
    private EntityManager entityManager; // Mock for the EntityManager

    @Mock
    private ProductFactory productFactory; // Mock for the product factory

    private ShoppingCart cart; // Instance of ShoppingCart for tests
    private Product book; // Declare as class variable
    private Product phone; // Declare as class variable
    private Product tablet; // Declare as class variable

    @BeforeEach
    public void setUp() {
        // Create a customer
        Customer customer = new Customer.CustomerBuilder("John", "Smith")
                .setBirthDate(LocalDate.of(1990, 1, 1))
                .setEmail("john@gmail.com")
                .setPhone("123456789")
                .setIdentificationNumber("DNI123")
                .build();

        // Simulate a shopping cart with the customer
        cart = new ShoppingCart(customer);

        // Create products using ProductFactory
        Product book = new Book(BigDecimal.valueOf(50.0), "A novel", "Default description");
        Product phone = new Electronic(BigDecimal.valueOf(300.0), "Smartphone", "Default description");

        // Verify that the products are not null
        assertNotNull(book);
        assertNotNull(phone);

        // Add products to the cart
        shoppingCartServices.addProductToCart(cart, book);
        shoppingCartServices.addProductToCart(cart, phone);
    }

    //------ Checkout tests ---------------------------- - -- --
    @Test
    public void testCheckout_Success() {
        // Create a mock order
        Order order = new StandardOrder(cart.getProducts());
        when(orderFactory.createOrder(cart.getProducts())).thenReturn(order);

        // Execute the checkout method
        Order resultOrder = shoppingCartServices.checkout(cart, true);

        // Verify that an order was created
        verify(orderFactory).createOrder(cart.getProducts());

        // Verify that the discount was applied
        verify(discountService).applyFirstPurchaseDiscount(order, true);

        // Verify that the returned order is not null
        assertNotNull(resultOrder);

        // Verify that the returned order is equal to the mock order
        assertEquals(order, resultOrder);

        // Optional: verify that the returned order has the expected products
        assertEquals(order.getProducts(), resultOrder.getProducts());
    }
    @Test
    public void testCheckout_NullCart_ThrowsIllegalArgumentException() {
        // Verify that the exception is thrown when the cart is null
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartServices.checkout(null, true);
        });

        assertEquals("Cart is null or empty", exception.getMessage());
    }

    @Test
    public void testCheckout_EmptyCart_ThrowsIllegalArgumentException() {
        ShoppingCart emptyCart = new ShoppingCart(cart.getCustomer()); // Create an empty cart

        // Verify that the exception is thrown when the cart is empty
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartServices.checkout(emptyCart, true);
        });

        assertEquals("Cart is null or empty", exception.getMessage());
    }

    @Test
    public void testCheckout_CreateOrderThrowsIllegalArgumentException() {
        // Simulate that creating the order throws an IllegalArgumentException
        when(orderFactory.createOrder(cart.getProducts())).thenThrow(new IllegalArgumentException("Invalid product"));

        // Verify that a RuntimeException is thrown when the order creation fails
        Exception exception = assertThrows(RuntimeException.class, () -> {
            shoppingCartServices.checkout(cart, true);
        });

        assertEquals("Failed to create order due to invalid arguments.", exception.getMessage());
    }

    @Test
    public void testCheckout_DiscountThrowsIllegalArgumentException() {
        // Create a mock order
        Order order = new StandardOrder(cart.getProducts());
        when(orderFactory.createOrder(cart.getProducts())).thenReturn(order);

        // Simulate that the discount service throws an IllegalArgumentException
        doThrow(new IllegalArgumentException("Invalid discount")).when(discountService).applyFirstPurchaseDiscount(order, true);

        // Verify that a RuntimeException is thrown when applying the discount fails
        Exception exception = assertThrows(RuntimeException.class, () -> {
            shoppingCartServices.checkout(cart, true);
        });

        assertEquals("Failed to apply discount due to invalid arguments.", exception.getMessage());
    }



    //------------------ tests for total price

    @Test
    public void testCalculateTotalPrice() {
        BigDecimal expectedTotal = BigDecimal.valueOf(350.0); // 50.0 + 300.0
        BigDecimal actualTotal = shoppingCartServices.calculateTotalPrice(cart);

        assertEquals(expectedTotal, actualTotal);
    }

    @Test
    public void testCalculateTotalPrice_NullCart_ReturnsZero() {
        BigDecimal total = shoppingCartServices.calculateTotalPrice(null);
        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    public void testCalculateTotalPrice_EmptyCart_ReturnsZero() {
        ShoppingCart emptyCart = new ShoppingCart(cart.getCustomer()); // Create an empty cart

        BigDecimal total = shoppingCartServices.calculateTotalPrice(emptyCart);
        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    public void testCalculateTotalPrice_ValidCart_ReturnsTotal() {
        BigDecimal expectedTotal = BigDecimal.valueOf(350.0); // 50.0 + 300.0
        BigDecimal actualTotal = shoppingCartServices.calculateTotalPrice(cart);

        assertEquals(expectedTotal, actualTotal);
    }


    //------------------ test addProductToCart
    @Test
    public void testAddProductToCart() {
        // Create a new product using ProductFactory
        Product newProduct = new Book(BigDecimal.valueOf(150.0), "Gaming laptop", "Default description");

        // Test adding a valid product
        boolean added = shoppingCartServices.addProductToCart(cart, newProduct);
        assertTrue(added, "The product was not added to the cart");
        assertTrue(cart.getProducts().contains(newProduct), "The product is not in the cart");
        assertEquals(3, cart.getProducts().size(), "The cart size is not as expected");

        // Test adding a null product
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartServices.addProductToCart(cart, null);
        });
        assertEquals("Product cannot be null.", exception.getMessage());

        // Test adding a null cart
        exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartServices.addProductToCart(null, newProduct);
        });
        assertEquals("Cart cannot be null.", exception.getMessage());

        // Test adding a product that already exists in the cart
        boolean alreadyExists = shoppingCartServices.addProductToCart(cart, newProduct);
        assertFalse(alreadyExists, "Allowed adding a product that already exists in the cart");
        assertEquals(3, cart.getProducts().size(), "The cart size should remain the same");
    }
//---------


    @Test
    public void testViewCart() {
        // Verify that the viewCart method executes without exceptions
        assertDoesNotThrow(() -> shoppingCartServices.viewCart(cart));
    }

    @Test
    public void testViewCartDetails() {
        // Capture the output from the method
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;
        System.setOut(printStream);

        // Execute the method
        shoppingCartServices.viewCartDetails(cart);

        // Restore the original output
        System.setOut(originalOut);

        // Check that the output contains the product descriptions
        String output = outputStream.toString();
        assertTrue(output.contains("A novel"), "Output should include product description");
        assertTrue(output.contains("Smartphone"), "Output should include product description");
    }

   /* @Test
    public void testBuyProducts() {
        // Execute the buyProducts method and verify that it does not throw exceptions
        assertDoesNotThrow(() -> shoppingCartServices.buyProducts(cart));
    }*/

    @Test
    public void testRemoveProductFromCart() {
        // Assume you have a product that has already been added to the cart
        Product productToRemove = cart.getProducts().get(0); // Get the first added product (A novel)

        // Verify that the product is present in the cart before removing it
        assertTrue(cart.getProducts().contains(productToRemove), "The product should be in the cart before removing it");

        // Call the removeProductFromCart method
        shoppingCartServices.removeProductFromCart(cart, productToRemove);

        // Verify that the product is no longer present in the cart after removing it
        assertFalse(cart.getProducts().contains(productToRemove), "The product should have been removed from the cart");
    }

    @Test
    public void testGetCartById() {
        Long cartId = cart.getId(); // Assuming you have a method to get the ID
        when(shoppingCartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        Optional<ShoppingCart> foundCart = shoppingCartServices.getCartById(cartId);

        assertTrue(foundCart.isPresent(), "The cart should be found.");
        assertEquals(cart, foundCart.get(), "The returned cart should be equal to the expected cart.");
        verify(shoppingCartRepository).findById(cartId);
    }

    @Test
    public void testGetAllCarts() {
        List<ShoppingCart> carts = Arrays.asList(cart);
        when(shoppingCartRepository.findAll()).thenReturn(carts);

        List<ShoppingCart> result = shoppingCartServices.getAllCarts();

        assertEquals(carts.size(), result.size(), "The number of returned carts should match the expected.");
        verify(shoppingCartRepository).findAll();
    }

    @Test
    public void testDeleteCartById() {
        Long cartId = cart.getId(); // Assuming you have a method to get the ID

        // Call the method to delete the cart
        shoppingCartServices.deleteCartById(cartId);

        // Verify that the deleteById method was called with the correct ID
        verify(shoppingCartRepository).deleteById(cartId);
    }

    @Test
    public void testGetCartsByStatus() {
        ShoppingCart.Status status = ShoppingCart.Status.DRAFT; // Assuming this status exists
        List<ShoppingCart> carts = Arrays.asList(cart);
        when(shoppingCartRepository.findByStatus(status)).thenReturn(carts);

        List<ShoppingCart> result = shoppingCartServices.getCartsByStatus(status);

        assertEquals(carts.size(), result.size(), "The number of returned carts should match the expected.");
        verify(shoppingCartRepository).findByStatus(status);
    }

    @Test
    public void testPrintCartInfoSortedByPrice() {
        // Create products directly in the test method
        Product book = new Book(BigDecimal.valueOf(50.0), "A novel", "Default description");
        Product phone = new Electronic(BigDecimal.valueOf(300.0), "Smartphone", "Default description");
        Product tablet = new Electronic(BigDecimal.valueOf(150.0), "Tablet", "Default description");

        // Verify that the products are not null
        assertNotNull(book, "The 'book' product should not be null");
        assertNotNull(phone, "The 'phone' product should not be null");
        assertNotNull(tablet, "The 'tablet' product should not be null");

        // Add products to the cart
        shoppingCartServices.addProductToCart(cart, book);
        shoppingCartServices.addProductToCart(cart, phone);
        shoppingCartServices.addProductToCart(cart, tablet);

        // Redirect standard output to capture what is printed
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Call the method being tested
        shoppingCartServices.printCartInfoSortedByPrice(cart);

        // Restore standard output
        System.setOut(originalOut);

        // Get the printed output
        String output = outputStream.toString();

        // Verify that the output contains products sorted by price
        String expectedOutput = String.format(
                "%-10s | %-3s | %-15s | %-20s | %.2f\n" +
                        "%-10s | %-3s | %-15s | %-20s | %.2f\n" +
                        "%-10s | %-3s | %-15s | %-20s | %.2f\n",
                cart.getId(), book.getId(), book.getType(), book.getName(), book.getPrice(),
                cart.getId(), tablet.getId(), tablet.getType(), tablet.getName(), tablet.getPrice(),
                cart.getId(), phone.getId(), phone.getType(), phone.getName(), phone.getPrice()
        );

        // Check that the output is correctly sorted and matches the expected
        assertTrue(output.contains(expectedOutput), "The cart output is not sorted correctly by price.");
    }
    @Test
    public void testAddProductToCart_NullCart() {
        Product product = new Book(BigDecimal.valueOf(50.0), "A novel", "Default description");

        assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartServices.addProductToCart(null, product);
        }, "Cart cannot be null.");
    }

    @Test
    public void testAddProductToCart_NullProduct() {
        assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartServices.addProductToCart(cart, null);
        }, "Product cannot be null.");
    }

    @Test
    public void testAddProductToCart_ProductAlreadyInCart() {
        Product existingProduct = cart.getProducts().get(0); // A product already in the cart

        boolean result = shoppingCartServices.addProductToCart(cart, existingProduct);

        assertFalse(result, "The product should already be in the cart and should not be added.");
    }

    @Test
    public void testRemoveProductFromCart_NullCart() {
        Product product = new Book(BigDecimal.valueOf(50.0), "A novel", "Default description");

        assertDoesNotThrow(() -> {
            shoppingCartServices.removeProductFromCart(null, product);
        }, "Should not throw an exception when the cart is null.");
    }

    @Test
    public void testRemoveProductFromCart_NullProduct() {
        assertDoesNotThrow(() -> {
            shoppingCartServices.removeProductFromCart(cart, null);
        }, "Should not throw an exception when the product is null.");
    }

    @Test
    public void testRemoveProductFromCart_ProductNotInCart() {
        Product productNotInCart = new Book(BigDecimal.valueOf(100.0), "New Book", "Default description");

        assertFalse(cart.getProducts().contains(productNotInCart), "The product should not be in the cart.");

        shoppingCartServices.removeProductFromCart(cart, productNotInCart); // Call the method

        assertFalse(cart.getProducts().contains(productNotInCart), "The product should not have been removed from the cart.");
    }

    @Test
    public void testGetCartsByCustomerId_NoCarts() {
        Long customerId = 1L; // Example customer ID
        when(shoppingCartRepository.findByCustomerId(customerId)).thenReturn(Collections.emptyList());

        List<ShoppingCart> result = shoppingCartServices.getCartsByCustomerId(customerId);

        assertTrue(result.isEmpty(), "The cart list should be empty.");
        verify(shoppingCartRepository).findByCustomerId(customerId);
    }

    @Test
    public void testDeleteCartById_NonExistentId() {
        Long nonExistentId = 999L; // ID that does not exist
        doNothing().when(shoppingCartRepository).deleteById(nonExistentId);

        shoppingCartServices.deleteCartById(nonExistentId); // Call the method

        verify(shoppingCartRepository).deleteById(nonExistentId); // Verify that the delete method was called
    }

    @Test
    public void testGetCartById_NotFound() {
        Long nonExistentId = 999L; // ID that does not exist
        when(shoppingCartRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Optional<ShoppingCart> result = shoppingCartServices.getCartById(nonExistentId);

        assertFalse(result.isPresent(), "Should not find the cart.");
        verify(shoppingCartRepository).findById(nonExistentId);
    }

    @Test
    public void testCheckout_NullCart() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartServices.checkout(null, false);
        });
        assertEquals("Cart is null or empty", exception.getMessage());
    }

    @Test
    public void testCheckout_EmptyCart() {
        cart.getProducts().clear(); // Clear the cart

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCartServices.checkout(cart, false);
        });
        assertEquals("Cart is null or empty", exception.getMessage());
    }
/*
    @Test
    public void testCheckout_NullOrderFactory() {
        // Initialize shoppingCartServices with null OrderFactory
        shoppingCartServices = new ShoppingCartServices(shoppingCartRepository, null, discountService); // Simulate a null OrderFactory

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            shoppingCartServices.checkout(cart, false);
        });
        assertEquals("Order factory is not initialized.", exception.getMessage());
    }

    @Test
    public void testCheckout_NullDiscountService() {
        // Initialize shoppingCartServices with null DiscountService
        shoppingCartServices = new ShoppingCartServices(shoppingCartRepository, orderFactory, null); // Simulate a null DiscountService

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            shoppingCartServices.checkout(cart, false);
        });
        assertEquals("Discount service is not initialized.", exception.getMessage());
    }*/

    @Test
    public void testCheckout_SuccessfulCheckout() {
        // Simulate successful behavior of OrderFactory
        Order order = mock(Order.class);
        when(orderFactory.createOrder(cart.getProducts())).thenReturn(order);
        when(order.getId()).thenReturn(1L); // Simulate that an order ID is generated

        Order resultOrder = shoppingCartServices.checkout(cart, false);

        assertNotNull(resultOrder);
        assertEquals(order.getId(), resultOrder.getId());
    }

    @Test
    public void testCheckout_FailedOrderCreation() {
        when(orderFactory.createOrder(cart.getProducts())).thenThrow(new IllegalArgumentException("Invalid product"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            shoppingCartServices.checkout(cart, false);
        });
        assertEquals("Failed to create order due to invalid arguments.", exception.getMessage());
    }

    @Test
    public void testCheckout_FailedDiscountApplication() {
        Order order = mock(Order.class);
        when(orderFactory.createOrder(cart.getProducts())).thenReturn(order);
        when(order.getId()).thenReturn(1L);

        doThrow(new IllegalArgumentException("Invalid discount")).when(discountService).applyFirstPurchaseDiscount(order, false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            shoppingCartServices.checkout(cart, false);
        });
        assertEquals("Failed to apply discount due to invalid arguments.", exception.getMessage());
    }



}