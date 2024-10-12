package com.shopi.shopping.services;
import com.shopi.shopping.exceptions.exceptionCustomer.CustomerNotFoundException;
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
    public Customer createCustomer(Customer customer) {
        logger.info("Creating a new customer: {}", customer);
        return customerRepository.save(customer);
    }


    // Fetch customer by ID
    public Optional<Customer> getCustomerById(Long id) {
        logger.info("Fetching customer with ID: {}", id);
        return customerRepository.findById(id); // Fetching customer by ID
    }
    // Update customer
    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        logger.info("Updating customer with ID: {}", id);
        Optional<Customer> existingCustomerOpt = customerRepository.findById(id);

        if (existingCustomerOpt.isPresent()) {
            Customer existingCustomer = existingCustomerOpt.get();

            // Actualiza los campos del cliente existente
            existingCustomer.setName(updatedCustomer.getName());
            existingCustomer.setLastName(updatedCustomer.getLastName());
            existingCustomer.setBirthDate(updatedCustomer.getBirthDate());
            existingCustomer.setEmail(updatedCustomer.getEmail());
            existingCustomer.setPhone(updatedCustomer.getPhone());
            existingCustomer.setIdentificationNumber(updatedCustomer.getIdentificationNumber());

            // Guarda y retorna el cliente actualizado
            return customerRepository.save(existingCustomer);
        } else {
            throw new CustomerNotFoundException("Customer not found with ID: " + id);
        }
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
    public List<Customer> getCustomersByFirstNameContaining(String name) {
        logger.info("Fetching customers with first name containing: {}", name);
        return customerRepository.findByNameContaining(name);
    }

    // Fetch all customers
    public List<Customer> getAllCustomers() {
        logger.info("Fetching all customers");
        return customerRepository.findAll(); // Fetching all customers
    }
}