package com.shopi.shopping.repositories;

import com.shopi.shopping.models.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repository interface for Product entity
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Custom query method to find products by name
    List<Product> findByName(String name);

    // Custom query method to find products by category
    List<Product> findByCategory(String category);

}

