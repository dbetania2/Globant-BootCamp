package com.shopi.shopping.services;
import com.shopi.shopping.models.Discount;
import com.shopi.shopping.models.Order;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.repositories.DiscountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    public BigDecimal calculateDiscount(Order order, Discount discount) {
        // This calculates the discount correctly
        BigDecimal discountAmount = order.getTotalAmount().multiply(discount.getRate()); // Assume discount rate is in BigDecimal
        logger.debug("Calculated discount for order ID {}: ${}", order.getId(), discountAmount);
        return discountAmount;
    }

    // Method to apply a first purchase discount based on a boolean value
    public void applyFirstPurchaseDiscount(Order order, boolean isFirstPurchase) {
        if (isFirstPurchase) {
            BigDecimal discountAmount = order.getTotalAmount().multiply(BigDecimal.valueOf(0.10)); // 10% discount
            order.setTotalAmount(order.getTotalAmount().subtract(discountAmount)); // Updates the total
            logger.info("Applied first purchase discount of 10.0% to order ID: {}", order.getId());
        } else {
            logger.info("No first purchase discount applied to order ID: {}", order.getId());
        }
    }

    public void applyDiscounts(Order order, List<Discount> discounts) {
        BigDecimal total = order.getTotalAmount();
        logger.info("Original total for order ID {}: ${}", order.getId(), total);

        // Only apply a discount if it is valid
        for (Discount discount : discounts) {
            if (isDiscountValid(discount)) {
                // Check if the discount applies to any of the products in the order
                boolean isApplicable = order.getProducts().stream()
                        .anyMatch(product -> product.getType().equals(discount.getCategory()));

                if (isApplicable) {
                    // Calculate and apply the discount
                    BigDecimal discountAmount = total.multiply(discount.getRate());
                    total = total.subtract(discountAmount);

                    // Add the applied discount to the order's list of discounts
                    order.getAppliedDiscounts().add(discount);  // Ensure that order has an initialized list

                    // Log the applied discount
                    logger.info("Discount applied to order ID {}: Discount ID: {}, Discount amount: ${}",
                            order.getId(), discount.getId(), discountAmount);
                } else {
                    logger.debug("Discount ID {} not applicable to order ID {}. Discount category: {} does not match products in the order.",
                            discount.getId(), order.getId(), discount.getCategory());
                }
            }
        }

        // Log the final total after applying discounts
        logger.info("Final total after applying discounts for order ID {}: ${}", order.getId(), total);

        order.setTotalAmount(total); // Ensure that the total is updated
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

    public BigDecimal applyProductDiscount(Product product, Discount discount) {
        if (isDiscountValid(discount)) {
            // Calculate the discount amount using BigDecimal
            BigDecimal discountAmount = discount.getRate().multiply(product.getPrice());

            // Set the new price of the product
            BigDecimal newPrice = product.getPrice().subtract(discountAmount);
            product.setPrice(newPrice);

            logger.info("Applied discount to product ID {}: New price: ${}", product.getId(), newPrice);
            return newPrice; // Return the new price
        }
        logger.info("No discount applied to product ID {}: Price remains unchanged: ${}", product.getId(), product.getPrice());
        return product.getPrice(); // No discount applied, so the price remains unchanged
    }

    //----------------------------
    // Create a discount
    public Discount createDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    // Update a discount
    public Discount updateDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    // Delete a discount by ID
    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }

    // Get all active discounts
    public List<Discount> findActiveDiscounts() {
        return discountRepository.findByEndDateAfter(LocalDate.now());
    }

    // Get a discount by ID
    public Optional<Discount> getDiscountDetails(Long id) {
        return discountRepository.findById(id);
    }



}
