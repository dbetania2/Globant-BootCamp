package com.shopi.shopping;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import com.shopi.shopping.controllers.OrderController;
import com.shopi.shopping.models.Order;
import com.shopi.shopping.models.StandardOrder;
import com.shopi.shopping.repositories.OrderRepository;
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


public class OrderControllerTest {

    // Injecting the OrderController and mocking the OrderRepository
    @InjectMocks
    private OrderController orderController;

    // Mocking the OrderRepository to avoid using a real database
    @Mock
    private OrderRepository orderRepository;

    // Set up the Mockito annotations before each test
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for creating a new order
    @Test
    public void testCreateOrder() {
        // Create a new instance of a concrete subclass of Order
        Order order = new StandardOrder(); // Ensure to initialize the object with valid data
        order.setId(1L); // Set an ID for the order

        // Mock the save method to return the created order
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Call the createOrder method on the controller
        ResponseEntity<Order> response = orderController.createOrder(order);

        // Verify the response status and body
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(order, response.getBody());
    }

    // Test for retrieving all orders
    @Test
    public void testGetAllOrders() {
        // Create sample orders
        Order order1 = new StandardOrder();
        Order order2 = new StandardOrder();
        List<Order> orders = Arrays.asList(order1, order2); // Create a list of orders

        // Mock the findAll method to return the sample orders
        when(orderRepository.findAll()).thenReturn(orders);

        // Call the getAllOrders method on the controller
        ResponseEntity<List<Order>> response = orderController.getAllOrders();

        // Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders, response.getBody());
    }

    // Test for retrieving an order by its ID (found case)
    @Test
    public void testGetOrderById_Found() {
        // Create an order and set its ID
        Order order = new StandardOrder();
        order.setId(1L);

        // Mock the findById method to return the order
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        // Call the getOrderById method on the controller
        ResponseEntity<Order> response = orderController.getOrderById(1L);

        // Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order, response.getBody());
    }

    // Test for retrieving an order by its ID (not found case)
    @Test
    public void testGetOrderById_NotFound() {
        // Mock the findById method to return an empty Optional
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call the getOrderById method on the controller
        ResponseEntity<Order> response = orderController.getOrderById(1L);

        // Verify the response status
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // Test for deleting an order by its ID (found case)
    @Test
    public void testDeleteOrderById_Found() {
        // Create an order and set its ID
        Order order = new StandardOrder();
        order.setId(1L);

        // Mock the findById method to return the order
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        // Call the deleteOrderById method on the controller
        ResponseEntity<Void> response = orderController.deleteOrderById(1L);

        // Verify that deleteById was called once and check the response status
        verify(orderRepository, times(1)).deleteById(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    // Test for deleting an order by its ID (not found case)
    @Test
    public void testDeleteOrderById_NotFound() {
        // Mock the findById method to return an empty Optional
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call the deleteOrderById method on the controller
        ResponseEntity<Void> response = orderController.deleteOrderById(1L);

        // Verify the response status and that deleteById was never called
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(orderRepository, never()).deleteById(anyLong());
    }

    // Test for retrieving orders by product ID
    @Test
    public void testGetOrdersByProductId() {
        // Create a sample order and add it to a list
        Order order = new StandardOrder();
        List<Order> orders = Arrays.asList(order);

        // Mock the findByProducts_Id method to return the sample orders
        when(orderRepository.findByProducts_Id(anyLong())).thenReturn(orders);

        // Call the getOrdersByProductId method on the controller
        ResponseEntity<List<Order>> response = orderController.getOrdersByProductId(1L);

        // Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders, response.getBody());
    }

    // Test for retrieving orders by discount ID
    @Test
    public void testGetOrdersByDiscountId() {
        // Create a sample order and add it to a list
        Order order = new StandardOrder();
        List<Order> orders = Arrays.asList(order);

        // Mock the findByAppliedDiscounts_Id method to return the sample orders
        when(orderRepository.findByAppliedDiscounts_Id(anyLong())).thenReturn(orders);

        // Call the getOrdersByDiscountId method on the controller
        ResponseEntity<List<Order>> response = orderController.getOrdersByDiscountId(1L);

        // Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders, response.getBody());
    }

    // Test for retrieving orders with a total amount greater than a specified value
    @Test
    public void testGetOrdersByTotalAmountGreaterThan() {
        // Create a sample order and add it to a list
        Order order = new StandardOrder();
        List<Order> orders = Arrays.asList(order);

        // Mock the findByTotalAmountGreaterThan method to return the sample orders
        when(orderRepository.findByTotalAmountGreaterThan(anyDouble())).thenReturn(orders);

        // Call the getOrdersByTotalAmountGreaterThan method on the controller
        ResponseEntity<List<Order>> response = orderController.getOrdersByTotalAmountGreaterThan(100.0);

        // Verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders, response.getBody());
    }
}