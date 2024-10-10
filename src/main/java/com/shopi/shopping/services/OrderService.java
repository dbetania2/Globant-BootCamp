package com.shopi.shopping.services;
import com.shopi.shopping.models.Discount;
import com.shopi.shopping.models.Order;
import com.shopi.shopping.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
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
        // Calculate the initial total of the order
        order.calculateTotal(); // This should only be called here to set the initial total

        // Apply discounts
        discountService.applyFirstPurchaseDiscount(order, isFirstPurchase);
        discountService.applyDiscounts(order, discounts);

        // Save the order in the repository
        orderRepository.save(order);

        // Log the final total
        System.out.println("Final total saved in the order: " + order.getTotalAmount());
    }



    // Save a new order to the database
    public Order saveOrder(Order order) {
        logger.info("Saving new order with {} products", order.getProducts().size());
        return orderRepository.save(order);
    }

    // Get order by ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
    }



    // Get a summary of all discounts applied to an order
    public String getDiscountSummary(Order order) {
        StringBuilder summary = new StringBuilder();
        BigDecimal totalDiscountAmount = BigDecimal.ZERO;

        summary.append("Discount Summary for Order ID: ").append(order.getId()).append("\n");
        summary.append("----------------------------------------------------\n");

        for (Discount discount : order.getAppliedDiscounts()) {
            BigDecimal discountAmount = discountService.calculateDiscount(order, discount); // Change here
            totalDiscountAmount = totalDiscountAmount.add(discountAmount);

            summary.append("Category: ").append(discount.getCategory())
                    .append(" | Rate: ").append(discount.getRate().multiply(BigDecimal.valueOf(100))).append("%")
                    .append(" | Amount: $").append(discountAmount).append("\n");
        }

        summary.append("----------------------------------------------------\n");
        summary.append("Total Discount: $").append(totalDiscountAmount).append("\n");

        return summary.toString();
    }


}
