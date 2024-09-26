package services;

import interfaces.DiscountStrategy;
import models.Discount;
import models.products.Product;

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
}
