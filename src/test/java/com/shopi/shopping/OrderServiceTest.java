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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;


@ExtendWith(MockitoExtension.class)
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
        productFactory = new ProductFactory();
        orderFactory = new OrderFactory();
    }


    @Test
    public void testApplyDiscountsAndSave_withDiscounts() {
        // Create products
        Product product1 = productFactory.createProduct("CLOTHING", "T-Shirt", BigDecimal.valueOf(50.0));
        Product product2 = productFactory.createProduct("ELECTRONIC", "Smartphone", BigDecimal.valueOf(300.0));
        List<Product> products = Arrays.asList(product1, product2);

        // Create an order
        Order order = orderFactory.createOrder(products);

        // Set the initial total amount for the order
        order.setTotalAmount(BigDecimal.valueOf(350.0)); // Ensure this is the correct total

        // Create a 10% discount for the test
        Discount discount1 = new Discount(BigDecimal.valueOf(0.10), "CLOTHING", "Percentage",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(10));
        List<Discount> discounts = Collections.singletonList(discount1);

        // Mock the behavior of discountService
        doCallRealMethod().when(discountService).applyFirstPurchaseDiscount(order, true);
        doCallRealMethod().when(discountService).applyDiscounts(eq(order), eq(discounts));

        // Mock the save method of orderRepository
        when(orderRepository.save(order)).thenReturn(order);

        // Apply discounts and save
        orderService.applyDiscountsAndSave(order, discounts, true);

        // Calculate the expected total amount after applying the discount
        BigDecimal expectedTotalAmount = new BigDecimal("315.00");
        BigDecimal actualTotalAmount = order.getTotalAmount();

        // Print expected and actual values for debugging
        System.out.println("Expected Total Amount: " + expectedTotalAmount);
        System.out.println("Actual Total Amount: " + actualTotalAmount);

        // Use assertEquals for comparison
        assertEquals(expectedTotalAmount, actualTotalAmount, "Total amount should be updated correctly after discounts");

        // Verify that the expected methods were called
        verify(discountService).applyFirstPurchaseDiscount(order, true);
        verify(discountService).applyDiscounts(eq(order), eq(discounts));
        verify(orderRepository).save(order);
    }


    @Test
    public void testGetOrderById_found() {
        Long orderId = 1L; // ID que queremos simular
        List<Product> products = Collections.emptyList(); // Define tus productos aquí
        Order expectedOrder = orderFactory.createOrder(products);
        expectedOrder.setId(orderId); // Asigna manualmente el ID

        // Mock the behavior of orderRepository
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));

        // Get the order by ID
        Order actualOrder = orderService.getOrderById(orderId);

        // Print the expected and actual orders for debugging
        System.out.println("Expected Order: " + expectedOrder);
        System.out.println("Actual Order: " + actualOrder);

        // Verify that the returned order is as expected
        assertEquals(expectedOrder, actualOrder);
        verify(orderRepository).findById(orderId);
    }



    @Test
    public void testGetOrderById_notFound() {
        Long orderId = 1L;

        // Mock the behavior of orderRepository
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Here we expect that calling getOrderById will trigger the interaction with the mock
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.getOrderById(orderId);
        });

        // Assert that the exception message is correct
        assertEquals("Order not found with ID: " + orderId, exception.getMessage());

        // Verify that the orderRepository's findById method was called with the correct ID
        verify(orderRepository).findById(orderId);
    }


    @Test
    public void testGetDiscountSummary() {
        // Create products and order
        Product product1 = productFactory.createProduct("BOOK", "Java Programming", BigDecimal.valueOf(40.0));
        Product product2 = productFactory.createProduct("CLOTHING", "Jeans", BigDecimal.valueOf(60.0));
        List<Product> products = Arrays.asList(product1, product2);
        Order order = orderFactory.createOrder(products);

        // Simulate applied discounts
        Discount discount1 = new Discount(BigDecimal.valueOf(0.1), "BOOK", "percentage", LocalDate.now().minusDays(1), LocalDate.now().plusDays(10)); // 10% discount
        Discount discount2 = new Discount(BigDecimal.valueOf(0.15), "CLOTHING", "percentage", LocalDate.now().minusDays(1), LocalDate.now().plusDays(10)); // 15% discount
        order.setAppliedDiscounts(Arrays.asList(discount1, discount2));

        // Mock the behavior of discountService
        when(discountService.calculateDiscount(eq(order), eq(discount1))).thenReturn(BigDecimal.valueOf(4.0)); // $4 discount on the book
        when(discountService.calculateDiscount(eq(order), eq(discount2))).thenReturn(BigDecimal.valueOf(9.0)); // $9 discount on clothing

        // Get the discount summary
        String summary = orderService.getDiscountSummary(order);

        // Verify that the summary contains the correct information
        assertTrue(summary.contains("Discount Summary for Order ID:"));
        assertTrue(summary.contains("Total Discount: $13.0")); // Expecting total discount from the two discounts
    }
}