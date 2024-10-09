package com.shopi.shopping.services;

import com.shopi.shopping.models.Discount;
import com.shopi.shopping.models.Order;
import com.shopi.shopping.models.StandardOrder;
import com.shopi.shopping.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final DiscountService discountService;

    @Autowired
    public OrderService(OrderRepository orderRepository, DiscountService discountService) {
        this.orderRepository = orderRepository;
        this.discountService = discountService;
    }

    // Method to apply discounts (including first purchase discount) and save the order
    public void applyDiscountsAndSave(Order order, List<Discount> discounts, boolean isFirstPurchase) {
        // Apply first purchase discount if necessary
        discountService.applyFirstPurchaseDiscount(order, isFirstPurchase);

        // Apply other discounts
        discountService.applyDiscounts(order, discounts);

        // Save the order after applying all discounts
        orderRepository.save(order);
        logger.info("Order ID: {} saved with total amount: ${}", order.getId(), order.getTotalAmount());
    }

    // Save a new order to the database
    public Order saveOrder(Order order) {
        logger.info("Saving new order with {} products", order.getProducts().size());
        return orderRepository.save(order);
    }

    // Get order by ID
    public Order getOrderById(Long orderId) {
        logger.info("Fetching order with ID: {}", orderId);
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
    }

    // Get a summary of all discounts applied to an order
    public String getDiscountSummary(Order order) {
        StringBuilder summary = new StringBuilder();
        double totalDiscountAmount = 0.0;

        summary.append("Discount Summary for Order ID: ").append(order.getId()).append("\n");
        summary.append("----------------------------------------------------\n");

        for (Discount discount : order.getAppliedDiscounts()) {
            double discountAmount = discountService.calculateDiscount(order, discount); // Change here
            totalDiscountAmount += discountAmount;

            summary.append("Category: ").append(discount.getCategory())
                    .append(" | Rate: ").append(discount.getRate() * 100).append("%")
                    .append(" | Amount: $").append(discountAmount).append("\n");
        }

        summary.append("----------------------------------------------------\n");
        summary.append("Total Discount: $").append(totalDiscountAmount).append("\n");

        return summary.toString();
    }
}
