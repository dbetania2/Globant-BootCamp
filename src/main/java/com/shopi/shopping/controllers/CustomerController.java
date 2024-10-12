package com.shopi.shopping.controllers;
import com.shopi.shopping.models.Customer;
import com.shopi.shopping.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    //------
    @Operation(summary = "Create a new customer", description = "Adds a new customer to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input.")
    })
    //------
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = customerService.createCustomer(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    //------
    @Operation(summary = "Update an existing customer", description = "Updates the details of an existing customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully."),
            @ApiResponse(responseCode = "404", description = "Customer not found.")
    })
    //------
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
        Customer updated = customerService.updateCustomer(id, updatedCustomer);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK); // Return 200 OK
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 Not Found
        }
    }

    //------
    @Operation(summary = "Delete a customer", description = "Removes a customer from the system by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Customer not found.")
    })
    //------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content
    }

    //------
    @Operation(summary = "Fetch all customers", description = "Retrieves a list of all customers.")
    @ApiResponse(responseCode = "200", description = "List of customers retrieved successfully.")
    //------
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK); // Return 200 OK
    }

    //------
    @Operation(summary = "Fetch a customer by ID", description = "Retrieves a specific customer by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Customer not found.")
    })
    //------
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customerOpt = customerService.getCustomerById(id);
        return customerOpt.map(customer -> new ResponseEntity<>(customer, HttpStatus.OK)) // Return 200 OK
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Return 404 Not Found
    }
}
