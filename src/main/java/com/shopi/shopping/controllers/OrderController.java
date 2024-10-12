package com.shopi.shopping.controllers;

import com.shopi.shopping.models.Order;
import com.shopi.shopping.repositories.OrderRepository;
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

    // Create a new order
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order savedOrder = orderRepository.save(order); // Save the order to the repository
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder); // Return response with CREATED status
    }

    // Retrieve all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderRepository.findAll(); // Get all orders from the repository
        return ResponseEntity.ok(orders); // Return response with OK status and the list of orders
    }

    // Retrieve an order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderRepository.findById(id); // Find order by ID
        return order.map(ResponseEntity::ok) // If found, return the order with OK status
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // If not found, return NOT FOUND status
    }

    // Retrieve orders by product ID
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Order>> getOrdersByProductId(@PathVariable Long productId) {
        List<Order> orders = orderRepository.findByProducts_Id(productId); // Find orders by product ID
        return ResponseEntity.ok(orders); // Return response with OK status and the list of orders
    }

    // Retrieve orders by applied discount ID
    @GetMapping("/discount/{discountId}")
    public ResponseEntity<List<Order>> getOrdersByDiscountId(@PathVariable Long discountId) {
        List<Order> orders = orderRepository.findByAppliedDiscounts_Id(discountId); // Find orders by discount ID
        return ResponseEntity.ok(orders); // Return response with OK status and the list of orders
    }

    // Retrieve orders with total amount greater than a specific value
    @GetMapping("/total-greater-than/{amount}")
    public ResponseEntity<List<Order>> getOrdersByTotalAmountGreaterThan(@PathVariable double amount) {
        List<Order> orders = orderRepository.findByTotalAmountGreaterThan(amount); // Find orders with total amount greater than specified
        return ResponseEntity.ok(orders); // Return response with OK status and the list of orders
    }

    // Delete an order by ID
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
