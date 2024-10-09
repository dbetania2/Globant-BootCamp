package com.shopi.shopping.services;
import com.shopi.shopping.models.Discount;
import com.shopi.shopping.models.Order;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.repositories.DiscountRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DiscountService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountService.class);

    @Autowired
    private DiscountRepository discountRepository;

    // Validate if a discount is still valid
    public boolean isDiscountValid(Discount discount) {
        // Logic to validate the discount
        boolean isValid = discount != null &&
                !LocalDate.now().isBefore(discount.getStartDate()) &&
                !LocalDate.now().isAfter(discount.getEndDate());

        logger.debug("Discount validity check for discount ID {}: {}", discount.getId(), isValid);
        return isValid;
    }

    // Calculate the discount for a given order
    public double calculateDiscount(Order order, Discount discount) {
        double discountAmount = order.getTotalAmount() * discount.getRate(); // This calculates the discount correctly
        logger.debug("Calculated discount for order ID {}: ${}", order.getId(), discountAmount);
        return discountAmount;
    }

    // Method to apply a first purchase discount based on a boolean value
    public void applyFirstPurchaseDiscount(Order order, boolean isFirstPurchase) {
        if (isFirstPurchase) {
            double discountAmount = order.getTotalAmount() * 0.10; // 10% discount
            order.setTotalAmount(order.getTotalAmount() - discountAmount); // Updates the total
            logger.info("Applied first purchase discount of 10.0% to order ID: {}", order.getId());
        } else {
            logger.info("No first purchase discount applied to order ID: {}", order.getId());
        }
    }

    public void applyDiscounts(Order order, List<Discount> discounts) {
        double originalAmount = order.getTotalAmount();
        double totalDiscountAmount = 0.0;

        logger.debug("Applying discounts to order ID: {} with original amount: ${}", order.getId(), originalAmount);

        for (Discount discount : discounts) {
            if (isDiscountValidForOrder(order, discount)) {
                order.addDiscount(discount);
                double discountAmount = originalAmount * discount.getRate();
                totalDiscountAmount += discountAmount;
                logger.debug("Applied discount ID {}: Amount deducted: ${}", discount.getId(), discountAmount);
            } else {
                logger.debug("Discount ID {} is not valid for order ID: {}", discount.getId(), order.getId());
            }
        }

        // Updates the order total
        order.setTotalAmount(originalAmount - totalDiscountAmount);
        logger.info("Total amount after applying discounts for order ID {}: ${}", order.getId(), order.getTotalAmount());
    }

    // Checks if a discount is valid for an order
    public boolean isDiscountValidForOrder(Order order, Discount discount) {
        boolean isValid = isDiscountValid(discount); // Ensure that this logic checks the validity of the discount
        logger.debug("Checking if discount ID {} is valid for order ID {}: {}", discount.getId(), order.getId(), isValid);
        return isValid;
    }

    // Checks if a discount is valid for a product
    private boolean isDiscountValidForProduct(Product product, Discount discount) {
        LocalDate now = LocalDate.now();
        boolean isValid = (now.isAfter(discount.getStartDate()) || now.isEqual(discount.getStartDate())) &&
                (now.isBefore(discount.getEndDate()) || now.isEqual(discount.getEndDate())) &&
                product.getType().equals(discount.getCategory());

        logger.debug("Checking product discount validity for product ID {}: {}", product.getId(), isValid);
        return isValid;
    }

    public double applyProductDiscount(Product product, Discount discount) {
        if (isDiscountValid(discount)) {
            double discountAmount = discount.getRate() * product.getPrice();
            product.setPrice(product.getPrice() - discountAmount);
            logger.info("Applied discount to product ID {}: New price: ${}", product.getId(), product.getPrice());
            return product.getPrice();
        }
        logger.info("No discount applied to product ID {}: Price remains unchanged: ${}", product.getId(), product.getPrice());
        return product.getPrice(); // No discount applied, so the price remains unchanged
    }
}
