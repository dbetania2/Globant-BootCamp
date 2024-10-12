package com.shopi.shopping.controllers;
import com.shopi.shopping.models.ShoppingCart;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.services.ProductService;
import com.shopi.shopping.services.ShoppingCartServices;
import com.shopi.shopping.repositories.ShoppingCartRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/shopping-carts")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ShoppingCartServices shoppingCartService;

    @Autowired
    private ProductService productService;

    //------
    @Operation(summary = "Create or update a shopping cart", description = "Creates a new shopping cart or updates an existing one.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Shopping cart created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid cart data.")
    })
    //------
    @PostMapping
    public ResponseEntity<ShoppingCart> createOrUpdateCart(@RequestBody ShoppingCart cart) {
        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCart);
    }

    //------
    @Operation(summary = "Get all shopping carts", description = "Retrieves a list of all shopping carts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shopping carts retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    //------
    @GetMapping
    public ResponseEntity<List<ShoppingCart>> getAllCarts() {
        List<ShoppingCart> carts = shoppingCartRepository.findAll();
        return ResponseEntity.ok(carts);
    }

    //------
    @Operation(summary = "Get a shopping cart by ID", description = "Retrieves a shopping cart by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shopping cart retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Shopping cart not found.")
    })
    //------
    @GetMapping("/{id}")
    public ResponseEntity<ShoppingCart> getCartById(@PathVariable Long id) {
        Optional<ShoppingCart> cart = shoppingCartRepository.findById(id);
        return cart.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    //------
    @Operation(summary = "Delete a shopping cart by ID", description = "Deletes a shopping cart by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Shopping cart deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Shopping cart not found.")
    })
    //------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartById(@PathVariable Long id) {
        if (shoppingCartRepository.findById(id).isPresent()) {
            shoppingCartRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //------
    @Operation(summary = "Get shopping carts by customer ID", description = "Retrieves shopping carts associated with a specific customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shopping carts retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Customer not found or has no carts.")
    })
    //------
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ShoppingCart>> getCartsByCustomerId(@PathVariable Long customerId) {
        List<ShoppingCart> carts = shoppingCartRepository.findByCustomerId(customerId);
        return ResponseEntity.ok(carts);
    }

    //------
    @Operation(summary = "Get shopping carts by status", description = "Retrieves shopping carts with a specific status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shopping carts retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "No carts found with the specified status.")
    })
    //------
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ShoppingCart>> getCartsByStatus(@PathVariable ShoppingCart.Status status) {
        List<ShoppingCart> carts = shoppingCartRepository.findByStatus(status);
        return ResponseEntity.ok(carts);
    }

    //------
    @Operation(summary = "Add a product to a shopping cart", description = "Adds a specified product to a shopping cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added to the shopping cart successfully."),
            @ApiResponse(responseCode = "404", description = "Shopping cart or product not found."),
            @ApiResponse(responseCode = "409", description = "Product already exists in the shopping cart.")
    })
    //------
    @PostMapping("/{cartId}/products/{productId}")
    public ResponseEntity<ShoppingCart> addProductToCart(@PathVariable Long cartId, @PathVariable Long productId) {
        // Retrieve the cart
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findById(cartId);
        if (!optionalCart.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Cart not found
        }
        ShoppingCart cart = optionalCart.get();

        // Retrieve the product using the productService
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Product not found
        }

        // Call the service to add the product to the cart
        boolean added = shoppingCartService.addProductToCart(cart, product);
        if (!added) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Product already exists in the cart
        }

        // Save the updated cart
        shoppingCartRepository.save(cart);
        return ResponseEntity.ok(cart); // Return the updated cart
    }
}