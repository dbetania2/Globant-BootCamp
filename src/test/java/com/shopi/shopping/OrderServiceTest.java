package com.shopi.shopping;
import com.shopi.shopping.factories.ProductFactory;
import com.shopi.shopping.factories.OrderFactory;
import com.shopi.shopping.models.Discount;
import com.shopi.shopping.models.Order;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.repositories.OrderRepository;
import com.shopi.shopping.services.DiscountService;
import com.shopi.shopping.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;


public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private OrderService orderService;

    private ProductFactory productFactory;
    private OrderFactory orderFactory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        productFactory = new ProductFactory();
        orderFactory = new OrderFactory();
    }

    /*@Test
    public void testApplyDiscountsAndSave_withDiscounts() {
        // Create products
        Product product1 = productFactory.createProduct("CLOTHING", "T-Shirt", 50.0);
        Product product2 = productFactory.createProduct("ELECTRONIC", "Smartphone", 300.0);
        List<Product> products = Arrays.asList(product1, product2);

        // Create an order
        Order order = orderFactory.createOrder(products);

        // Create a valid 10% discount for the test
        Discount discount1 = new Discount(0.1, "BOOK", "percentage", LocalDate.now().minusDays(1), LocalDate.now().plusDays(10)); // 10% discount
        List<Discount> discounts = Collections.singletonList(discount1);

        // Mock the behavior of discountService
        doNothing().when(discountService).applyFirstPurchaseDiscount(order, true);
        doNothing().when(discountService).applyDiscounts(eq(order), eq(discounts));

        // Save the mocked order
        when(orderRepository.save(order)).thenReturn(order);

        // Apply discounts and save
        orderService.applyDiscountsAndSave(order, discounts, true);

        // Verify that the expected methods were called
        verify(discountService).applyFirstPurchaseDiscount(order, true);
        verify(discountService).applyDiscounts(eq(order), eq(discounts));
        verify(orderRepository).save(order);

        // Assert that the total has been updated correctly
        assertEquals(315.0, order.getTotalAmount(), 0.01); // This is correct if you expect to apply a 10% discount
    }*/

    @Test
    public void testGetOrderById_found() {
        Long orderId = 1L;
        Order expectedOrder = orderFactory.createOrder(Collections.emptyList());

        // Mock the behavior of orderRepository
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));

        // Get the order by ID
        Order actualOrder = orderService.getOrderById(orderId);

        // Verify that the returned order is as expected
        assertEquals(expectedOrder, actualOrder);
        verify(orderRepository).findById(orderId);
    }

    @Test
    public void testGetOrderById_notFound() {
        Long orderId = 1L;

        // Mock the behavior of orderRepository
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Verify that an exception is thrown when searching for a non-existing order
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.getOrderById(orderId);
        });

        assertEquals("Order not found with ID: " + orderId, exception.getMessage());
        verify(orderRepository).findById(orderId);
    }

    @Test
    public void testGetDiscountSummary() {
        // Create products and order
        Product product1 = productFactory.createProduct("BOOK", "Java Programming", 40.0);
        Product product2 = productFactory.createProduct("CLOTHING", "Jeans", 60.0);
        List<Product> products = Arrays.asList(product1, product2);
        Order order = orderFactory.createOrder(products);

        // Simulate applied discounts with the new constructor
        Discount discount1 = new Discount(0.1, "BOOK", "percentage", LocalDate.now().minusDays(1), LocalDate.now().plusDays(10)); // 10% discount
        Discount discount2 = new Discount(0.15, "CLOTHING", "percentage", LocalDate.now().minusDays(1), LocalDate.now().plusDays(10)); // 15% discount
        order.setAppliedDiscounts(Arrays.asList(discount1, discount2));

        // Mock the behavior of discountService
        when(discountService.calculateDiscount(order, discount1)).thenReturn(4.0); // $4 discount on the book
        when(discountService.calculateDiscount(order, discount2)).thenReturn(9.0); // $9 discount on clothing

        // Get the discount summary
        String summary = orderService.getDiscountSummary(order);

        // Verify that the summary contains the correct information
        assertTrue(summary.contains("Discount Summary for Order ID:"));
        assertTrue(summary.contains("Total Discount: $13.0"));
    }
}
