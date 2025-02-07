package com.shopi.shopping.repositories;

import com.shopi.shopping.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Find a customer by their email address
    Optional<Customer> findByEmail(String email);

    // Find all customers by their last name
    List<Customer> findByLastName(String lastName);

    // Find all customers whose first name contains a specific string
    List<Customer> findByNameContaining(String name);

    // Find all customers (for example, to fetch all customers)
    List<Customer> findAll(); // This method is inherited from JpaRepository
}