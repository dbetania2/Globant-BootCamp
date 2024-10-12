package com.shopi.shopping.controllers;

import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Get a product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable long id) {
        Product product = productService.getProductById(id);
        return (product != null) ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    // Create a new product
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestParam String category,
            @RequestParam String name,
            @RequestParam BigDecimal price) {
        Product newProduct = productService.createProduct(category, name, price);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    // Update an existing product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable long id,
            @RequestParam BigDecimal price,
            @RequestParam String name,
            @RequestParam String description) {
        Product existingProduct = productService.getProductById(id);
        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }
        existingProduct.setPrice(price);
        existingProduct.setName(name);
        existingProduct.setDescription(description);
        productService.updateProduct(existingProduct);
        return ResponseEntity.ok(existingProduct);
    }

    // Delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        if (productService.getProductById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}