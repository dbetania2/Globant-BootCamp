package com.shopi.shopping.services;
import com.shopi.shopping.models.Customer;
import com.shopi.shopping.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Create a new customer
    public Customer createCustomer(Customer.CustomerBuilder builder) {
        logger.info("Creating a new customer: {}", builder);
        Customer customer = builder.build();
        return customerRepository.save(customer);
    }

    // Fetch customer by ID
    public Optional<Customer> getCustomerById(Long id) {
        logger.info("Fetching customer with ID: {}", id);
        return customerRepository.findById(id); // Fetching customer by ID
    }

    // Update customer
    public void updateCustomer(Customer customer) {
        logger.info("Updating customer with ID: {}", customer.getId());
        customerRepository.save(customer); // Updating the customer in the repository
    }

    // Delete customer
    public void deleteCustomer(Long id) {
        logger.info("Deleting customer with ID: {}", id);
        customerRepository.deleteById(id); // Deleting the customer by ID
    }

    // Fetch customer by email
    public Optional<Customer> getCustomerByEmail(String email) {
        logger.info("Fetching customer with email: {}", email);
        return customerRepository.findByEmail(email);
    }

    // Fetch customers by last name
    public List<Customer> getCustomersByLastName(String lastName) {
        logger.info("Fetching customers with last name: {}", lastName);
        return customerRepository.findByLastName(lastName);
    }

    // Fetch customers by first name containing a specific string
    public List<Customer> getCustomersByFirstNameContaining(String firstName) {
        logger.info("Fetching customers with first name containing: {}", firstName);
        return customerRepository.findByFirstNameContaining(firstName);
    }

    // Fetch all customers
    public List<Customer> getAllCustomers() {
        logger.info("Fetching all customers");
        return customerRepository.findAll(); // Fetching all customers
    }
}