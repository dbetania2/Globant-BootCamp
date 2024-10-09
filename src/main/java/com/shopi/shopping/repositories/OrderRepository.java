package com.shopi.shopping.repositories;
import com.shopi.shopping.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find all orders with a specific product ID (useful for tracking product sales)
    List<Order> findByProducts_Id(Long productId);

    // Find all orders with a specific applied discount (useful for analyzing discount usage)
    List<Order> findByAppliedDiscounts_Id(Long discountId);

    // Find orders with total amount greater than a specific value
    List<Order> findByTotalAmountGreaterThan(double amount);


}
