package com.shopi.shopping.services;

import com.shopi.shopping.interfaces.DiscountStrategy;
import com.shopi.shopping.models.Discount;
import com.shopi.shopping.models.Order;
import com.shopi.shopping.models.products.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class DiscountService implements DiscountStrategy {

    private static final Logger logger = LoggerFactory.getLogger(DiscountService.class);

    // Apply discount for a specific product
    @Override
    public double applyProductDiscount(Product product, Discount discount) {
        // Ensure the discount is valid for the current date
        LocalDate currentDate = LocalDate.now();
        if (currentDate.isBefore(discount.getStartDate()) || currentDate.isAfter(discount.getEndDate())) {
            logger.error("Discount is not valid for the current date: {}", currentDate);    //logger------------
            throw new IllegalArgumentException("The discount is not valid for the current date.");
        }

        // Calculate the discounted price
        double discountedPrice = product.getPrice() * (1 - discount.getRate());
        logger.info("Discount applied to product {}: original price {}, discounted price {}",    //logger------------
                product.getName(), product.getPrice(), discountedPrice);
        return discountedPrice; // Return the calculated discounted price
    }

    // Apply discount for first purchase
    private static final double FIRST_PURCHASE_DISCOUNT = 0.10;

    public double applyFirstPurchaseDiscount(Order order, boolean isFirstPurchase) {
        if (isFirstPurchase) {
            double originalTotal = order.getTotalAmount(); // Store the original total for logging
            double discountedAmount = originalTotal * (1 - FIRST_PURCHASE_DISCOUNT);
            order.setTotalAmount(discountedAmount); // Update total after discount
            logger.info("First purchase discount applied. Original total: {}, New total: {}",    //logger------------
                    originalTotal, discountedAmount);
        }
        return order.getTotalAmount();
    }
}
