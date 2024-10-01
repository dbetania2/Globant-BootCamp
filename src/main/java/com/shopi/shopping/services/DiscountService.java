package com.shopi.shopping.services;

import com.shopi.shopping.interfaces.DiscountStrategy;
import com.shopi.shopping.models.Discount;
import com.shopi.shopping.models.Order;
import com.shopi.shopping.models.products.Product;

import java.time.LocalDate;

public class DiscountService implements DiscountStrategy {

    @Override
    public double applyProductDiscount(Product product, Discount discount) {
        // Ensure the discount is valid for the current date
        LocalDate currentDate = LocalDate.now();
        if (currentDate.isBefore(discount.getStartDate()) || currentDate.isAfter(discount.getEndDate())) {
            throw new IllegalArgumentException("The discount is not valid for the current date.");
        }

        // Calculate the discounted price
        double discountedPrice = product.getPrice() * (1 - discount.getRate());
        return discountedPrice; // Return the calculated discounted price
    }


    // Apply discount for first purchase
    private static final double FIRST_PURCHASE_DISCOUNT = 0.10;
    public double applyFirstPurchaseDiscount(Order order, boolean isFirstPurchase) {
        if (isFirstPurchase) {
            double discountedAmount = order.getTotalAmount() * (1 - FIRST_PURCHASE_DISCOUNT);
            order.setTotalAmount(discountedAmount); // Update total after discount
            System.out.println("First purchase discount applied");
        }
        return order.getTotalAmount();
    }
}
