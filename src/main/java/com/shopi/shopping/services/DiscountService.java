package com.shopi.shopping.services;

import com.shopi.shopping.interfaces.DiscountStrategy;
import com.shopi.shopping.models.Discount;
import com.shopi.shopping.models.Order;
import com.shopi.shopping.models.products.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service

public class DiscountService implements DiscountStrategy {

    private static final Logger logger = LoggerFactory.getLogger(DiscountService.class);    //logger------------
    private static final BigDecimal FIRST_PURCHASE_DISCOUNT = new BigDecimal("0.10");

    @Override
    public double applyProductDiscount(Product product, Discount discount) {
        LocalDate currentDate = LocalDate.now();
        if (currentDate.isBefore(discount.getStartDate()) || currentDate.isAfter(discount.getEndDate())) {
            logger.error("Discount is not valid for the current date: {}", currentDate);    //logger-----------------
            throw new IllegalArgumentException("The discount is not valid for the current date.");
        }

        BigDecimal originalPrice = BigDecimal.valueOf(product.getPrice());
        BigDecimal discountRate = BigDecimal.valueOf(1).subtract(BigDecimal.valueOf(discount.getRate()));
        BigDecimal discountedPrice = originalPrice.multiply(discountRate).setScale(2, RoundingMode.HALF_UP);

        logger.info("Discount applied to product {}: original price {}, discounted price {}",    //logger------------
                product.getName(), originalPrice, discountedPrice);

        return discountedPrice.doubleValue();
    }

    public double applyFirstPurchaseDiscount(Order order, boolean isFirstPurchase) {
        if (isFirstPurchase) {
            BigDecimal originalTotal = BigDecimal.valueOf(order.getTotalAmount());
            BigDecimal discountedAmount = originalTotal.multiply(FIRST_PURCHASE_DISCOUNT);
            BigDecimal newTotal = originalTotal.subtract(discountedAmount).setScale(2, RoundingMode.HALF_UP);

            order.setTotalAmount(newTotal.doubleValue());

            Discount firstPurchaseDiscount = new Discount(FIRST_PURCHASE_DISCOUNT.doubleValue(),
                    "First Purchase Discount", LocalDate.now(), LocalDate.now().plusDays(30));

            addDiscount(order, firstPurchaseDiscount);

            logger.info("First purchase discount applied. Original total: {}, New total: {}",    //logger------------
                    originalTotal, newTotal);
        }
        return order.getTotalAmount();
    }

    public double applyDiscounts(Order order, List<Discount> discounts) {
        BigDecimal total = BigDecimal.valueOf(order.getTotalAmount());
        Set<Discount> alreadyApplied = new HashSet<>(order.getAppliedDiscounts()); // Evitar duplicados

        for (Discount discount : discounts) {
            if (!alreadyApplied.contains(discount) && discount.isValid()) {
                BigDecimal discountAmount = total.multiply(BigDecimal.valueOf(discount.getRate()));
                total = total.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);

                // Agregar descuento aplicado
                addDiscount(order, discount); // Asegúrate de que esto esté aquí

                logger.info("Applied discount: {} for order. New total: {}", discount.getRate(), total);    //logger------------
            } else if (alreadyApplied.contains(discount)) {
                logger.warn("Discount {} already applied to order: {}", discount, order.getId());
            } else {
                logger.error("Discount is not valid for the current date: {}", LocalDate.now());    //logger------------
            }
        }

        order.setTotalAmount(total.doubleValue());
        return total.doubleValue();
    }



    public double calculateDiscount(Order order, Discount discount) {
        return BigDecimal.valueOf(order.getTotalAmount())
                .multiply(BigDecimal.valueOf(discount.getRate()))
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public void addDiscount(Order order, Discount discount) {
        order.getAppliedDiscounts().add(discount);
    }
}