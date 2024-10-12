package com.shopi.shopping.controllers;

import com.shopi.shopping.models.Discount;
import com.shopi.shopping.services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    // Create a new discount
    @PostMapping
    public ResponseEntity<Discount> createDiscount(@RequestBody Discount discount) {
        Discount createdDiscount = discountService.createDiscount(discount);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDiscount);
    }

    // Update an existing discount by ID
    @PutMapping("/{id}")
    public ResponseEntity<Discount> updateDiscount(@PathVariable Long id, @RequestBody Discount discount) {
        discount.setId(id); // Ensure the ID is set on the discount object
        Discount updatedDiscount = discountService.updateDiscount(discount);
        return ResponseEntity.ok(updatedDiscount);
    }

    // Delete a discount by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }

    // Retrieve all active discounts
    @GetMapping
    public ResponseEntity<List<Discount>> getAllDiscounts() {
        List<Discount> discounts = discountService.findActiveDiscounts();
        return ResponseEntity.ok(discounts);
    }

    // Retrieve a discount by ID
    @GetMapping("/{id}")
    public ResponseEntity<Discount> getDiscount(@PathVariable Long id) {
        return discountService.getDiscountDetails(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
