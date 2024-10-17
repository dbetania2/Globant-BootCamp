package com.shopi.shopping.controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);


    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //------
    @Operation(summary = "Get all products", description = "Fetches a list of all available products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error."),
            @ApiResponse(responseCode = "204", description = "empty list.")
    })
    //------
    @GetMapping // Find all products
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        logger.info("Fetched products: " + products);

        // Si la lista está vacía, devuelve un No Content (204)
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Devuelve la lista con un código 200 (OK)
        return ResponseEntity.ok(products);
    }



    //------
    @Operation(summary = "Get a product by ID", description = "Fetches a specific product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Product not found.")
    })
    //------
    @GetMapping("/{id}") //---------"Find a product given unique identifier"----------
    public ResponseEntity<Product> getProductById(@PathVariable long id) {
        Product product = productService.getProductById(id);
        return (product != null) ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    //------
    @Operation(summary = "Create a new product", description = "Creates a new product and saves it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid product details.")
    })
    //------
    @PostMapping //---------"Create a product"------
    public ResponseEntity<Product> createProduct(
            @RequestParam String category,
            @RequestParam String name,
            @RequestParam BigDecimal price) {
        Product newProduct = productService.createProduct(category, name, price);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    //------
    @Operation(summary = "Update an existing product", description = "Updates the details of an existing product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully."),
            @ApiResponse(responseCode = "404", description = "Product not found."),
            @ApiResponse(responseCode = "400", description = "Invalid product details.")
    })
    //------
    @PutMapping("/{id}") //---------"Update a product"------
    public ResponseEntity<Product> updateProduct(
            @PathVariable long id,
            @RequestParam BigDecimal price,
            @RequestParam String name,
            @RequestParam String description) {

        // Fetch the existing product
        Product existingProduct = productService.getProductById(id);

        // Check if the product exists
        if (existingProduct == null) {
            return ResponseEntity.notFound().build(); // Return 404 if the product does not exist
        }

        // Update the product details
        existingProduct.setPrice(price);
        existingProduct.setName(name);
        existingProduct.setDescription(description);

        // Update the product in the service
        productService.updateProduct(existingProduct);

        return ResponseEntity.ok(existingProduct); // Return the updated product
    }


    //------
    @Operation(summary = "Delete a product by ID", description = "Deletes a specific product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Product not found.")
    })
    //------
    @DeleteMapping("/{id}")  //---------"Delete a Product"------
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        try {
            productService.deleteProductWithCache(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
