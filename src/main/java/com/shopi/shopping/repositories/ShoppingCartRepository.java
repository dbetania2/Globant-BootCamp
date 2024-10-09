package com.shopi.shopping.repositories;
import com.shopi.shopping.models.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    // Method to save or update a shopping cart
    @Override
    ShoppingCart save(ShoppingCart cart);

    // Method to find a shopping cart by its ID
    @Override
    Optional<ShoppingCart> findById(Long id);

    // Method to retrieve a list of all shopping carts
    @Override
    List<ShoppingCart> findAll();

    // Method to delete a shopping cart by its ID
    @Override
    void deleteById(Long id);

    // Custom method to find shopping carts by customer ID
    List<ShoppingCart> findByCustomerId(Long customerId); // Get all carts for a specific customer

    // Custom method to find shopping carts by status
    List<ShoppingCart> findByStatus(ShoppingCart.Status status); // Get all carts with a specific status
}
