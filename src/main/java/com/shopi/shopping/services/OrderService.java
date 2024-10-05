package com.shopi.shopping.services;

import com.shopi.shopping.models.Discount;
import com.shopi.shopping.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);    //logger------------
    private final DiscountService discountService;

    // Constructor to initialize DiscountService
    public OrderService(DiscountService discountService) {
        this.discountService = discountService;
    }

    // Method to apply discounts to the order
    public void applyDiscounts(Order order, List<Discount> discounts) {
        logger.info("Applying discounts to order: {}", order.getId());    //logger------------
        for (Discount discount : discounts) {
            if (discount.isValid()) {
                double discountAmount = discountService.calculateDiscount(order, discount); // Use DiscountService to calculate
                order.setTotalAmount(order.getTotalAmount() - discountAmount); // Apply the discount to the order total
                addDiscount(order, discount); // Add the discount to the applied discounts
                logger.info("Applied discount of {}% on category {}: Amount deducted: {}",    //logger-------------------
                        discount.getRate() * 100, discount.getCategory(), discountAmount);
            } else {
                logger.warn("Discount is not valid: {} on order: {}", discount, order.getId());    //logger------------
            }
        }
    }

    // Method to add a discount to the list of applied discounts for the order
    public void addDiscount(Order order, Discount discount) {
        order.getAppliedDiscounts().add(discount); // Add the discount to the list of applied discounts
    }

    // Method to obtain a summary of the applied discounts
    public String getDiscountSummary(Order order) {
        StringBuilder summary = new StringBuilder("Applied discounts:\n");
        for (Discount discount : order.getAppliedDiscounts()) {
            summary.append(discount.toString()).append("\n"); // Append each discount's information to the summary
        }
        summary.append("Total after discounts: ").append(order.getTotalAmount()); // Append the total amount after discounts
        return summary.toString(); // Return the summary
    }
}
