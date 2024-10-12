package com.shopi.shopping.controllers;

import com.shopi.shopping.models.Discount;
import com.shopi.shopping.services.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/discounts") // Base URL for the discount API
public class DiscountController {

    @Autowired
    private DiscountService discountService; // Service layer for discount-related operations

    //------
    @Operation(summary = "Create a new discount", description = "Adds a new discount to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Discount created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid discount input.")
    })
    //------
    @PostMapping
    public ResponseEntity<Discount> createDiscount(@RequestBody Discount discount) {
        Discount createdDiscount = discountService.createDiscount(discount);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDiscount);
    }

    //------
    @Operation(summary = "Update an existing discount", description = "Updates the details of an existing discount.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discount updated successfully."),
            @ApiResponse(responseCode = "404", description = "Discount not found.")
    })
    //------
    @PutMapping("/{id}")
    public ResponseEntity<Discount> updateDiscount(@PathVariable Long id, @RequestBody Discount discount) {
        discount.setId(id); // Ensure the ID is set on the discount object
        Discount updatedDiscount = discountService.updateDiscount(discount);
        return ResponseEntity.ok(updatedDiscount);
    }

    //------
    @Operation(summary = "Delete a discount", description = "Removes a discount from the system by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Discount deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Discount not found.")
    })
    //------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    //------
    @Operation(summary = "Retrieve all active discounts", description = "Fetches a list of all active discounts.")
    @ApiResponse(responseCode = "200", description = "List of active discounts retrieved successfully.")
    //------
    @GetMapping
    public ResponseEntity<List<Discount>> getAllDiscounts() {
        List<Discount> discounts = discountService.findActiveDiscounts();
        return ResponseEntity.ok(discounts); // Return 200 OK
    }

    //------
    @Operation(summary = "Retrieve a discount by ID", description = "Fetches a specific discount by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discount retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Discount not found.")
    })
    //------
    @GetMapping("/{id}")
    public ResponseEntity<Discount> getDiscount(@PathVariable Long id) {
        return discountService.getDiscountDetails(id)
                .map(ResponseEntity::ok) // Return 200 OK if found
                .orElse(ResponseEntity.notFound().build()); // Return 404 Not Found
    }
}