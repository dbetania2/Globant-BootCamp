package com.shopi.shopping.controllers;

import com.shopi.shopping.models.Order;
import com.shopi.shopping.repositories.OrderRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository; // Automatically injects the OrderRepository

    //------
    @Operation(summary = "Create a new order", description = "Creates a new order and saves it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid order details.")
    })
    //------
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order savedOrder = orderRepository.save(order); // Save the order to the repository
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder); // Return response with CREATED status
    }

    //------
    @Operation(summary = "Retrieve all orders", description = "Fetches a list of all orders.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    //------
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderRepository.findAll(); // Get all orders from the repository
        return ResponseEntity.ok(orders); // Return response with OK status and the list of orders
    }

    //------
    @Operation(summary = "Retrieve an order by ID", description = "Fetches a specific order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Order not found.")
    })
    //------
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderRepository.findById(id); // Find order by ID
        return order.map(ResponseEntity::ok) // If found, return the order with OK status
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // If not found, return NOT FOUND status
    }

    //------
    @Operation(summary = "Retrieve orders by product ID", description = "Fetches all orders containing a specific product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "No orders found for the specified product ID.")
    })
    //------
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Order>> getOrdersByProductId(@PathVariable Long productId) {
        List<Order> orders = orderRepository.findByProducts_Id(productId); // Find orders by product ID
        return ResponseEntity.ok(orders); // Return response with OK status and the list of orders
    }

    //------
    @Operation(summary = "Retrieve orders by discount ID", description = "Fetches all orders associated with a specific discount.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "No orders found for the specified discount ID.")
    })
    //------
    @GetMapping("/discount/{discountId}")
    public ResponseEntity<List<Order>> getOrdersByDiscountId(@PathVariable Long discountId) {
        List<Order> orders = orderRepository.findByAppliedDiscounts_Id(discountId); // Find orders by discount ID
        return ResponseEntity.ok(orders); // Return response with OK status and the list of orders
    }

    //-*-----
    @Operation(summary = "Retrieve orders with total amount greater than a specified value", description = "Fetches all orders where the total amount exceeds the provided value.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "No orders found with total amount greater than specified.")
    })
    //------
    @GetMapping("/total-greater-than/{amount}")
    public ResponseEntity<List<Order>> getOrdersByTotalAmountGreaterThan(@PathVariable double amount) {
        List<Order> orders = orderRepository.findByTotalAmountGreaterThan(amount); // Find orders with total amount greater than specified
        return ResponseEntity.ok(orders); // Return response with OK status and the list of orders
    }

    //------
    @Operation(summary = "Delete an order by ID", description = "Deletes a specific order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Order not found.")
    })
    //------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        if (orderRepository.findById(id).isPresent()) { // Check if the order exists
            orderRepository.deleteById(id); // Delete the order
            return ResponseEntity.noContent().build(); // Return NO CONTENT status
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // If not found, return NOT FOUND status
        }
    }
}