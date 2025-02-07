package com.shopi.shopping.repositories;
import com.shopi.shopping.models.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    // Get discounts by category
    List<Discount> findByCategory(String category);

    long countByCategory(String category);

    // Method to get active discounts
    List<Discount> findByEndDateAfter(LocalDate date);
}

