package com.shopi.shopping.controllers;
import com.shopi.shopping.models.Customer;
import com.shopi.shopping.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers") // Base URL for the customer API
public class CustomerController {

    private final CustomerService customerService; // Service layer for customer-related operations

    // Constructor for dependency injection of the CustomerService
    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Create a new customer
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        // Call the service method to create a new customer and store the result
        Customer createdCustomer = customerService.createCustomer(customer);
        // Return the created customer with a 201 Created status
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    // Update an existing customer
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
        // Call the service method to update the customer with the given ID
        Customer updated = customerService.updateCustomer(id, updatedCustomer);

        // Check if the update was successful
        if (updated != null) {
            // Return the updated customer with a 200 OK status
            return new ResponseEntity<>(updated, HttpStatus.OK); // Return 200 OK
        } else {
            // Return a 404 Not Found status if the customer does not exist
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 Not Found
        }
    }

    // Delete a customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        // Call the service method to delete the customer with the given ID
        customerService.deleteCustomer(id);
        // Assuming that if the customer was not found, nothing happens
        // Return a 204 No Content status indicating successful deletion
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content
    }

    // Fetch all customers
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        // Call the service method to get a list of all customers
        List<Customer> customers = customerService.getAllCustomers();
        // Return the list of customers with a 200 OK status
        return new ResponseEntity<>(customers, HttpStatus.OK); // Return 200 OK
    }

    // Fetch a customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        // Call the service method to get a customer by ID
        Optional<Customer> customerOpt = customerService.getCustomerById(id);
        // Return the customer if found, or a 404 Not Found status if not
        return customerOpt.map(customer -> new ResponseEntity<>(customer, HttpStatus.OK)) // Return 200 OK
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Return 404 Not Found
    }
}
