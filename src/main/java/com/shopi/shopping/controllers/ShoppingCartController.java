package com.shopi.shopping.controllers;
import com.shopi.shopping.models.ShoppingCart;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.services.ProductService;
import com.shopi.shopping.services.ShoppingCartServices;
import com.shopi.shopping.repositories.ShoppingCartRepository;
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

    // Create or update a shopping cart
    @PostMapping
    public ResponseEntity<ShoppingCart> createOrUpdateCart(@RequestBody ShoppingCart cart) {
        ShoppingCart savedCart = shoppingCartRepository.save(cart);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCart);
    }

    // Get all shopping carts
    @GetMapping
    public ResponseEntity<List<ShoppingCart>> getAllCarts() {
        List<ShoppingCart> carts = shoppingCartRepository.findAll();
        return ResponseEntity.ok(carts);
    }

    // Get a shopping cart by ID
    @GetMapping("/{id}")
    public ResponseEntity<ShoppingCart> getCartById(@PathVariable Long id) {
        Optional<ShoppingCart> cart = shoppingCartRepository.findById(id);
        return cart.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Delete a shopping cart by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartById(@PathVariable Long id) {
        if (shoppingCartRepository.findById(id).isPresent()) {
            shoppingCartRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Get shopping carts by customer ID
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ShoppingCart>> getCartsByCustomerId(@PathVariable Long customerId) {
        List<ShoppingCart> carts = shoppingCartRepository.findByCustomerId(customerId);
        return ResponseEntity.ok(carts);
    }

    // Get shopping carts by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ShoppingCart>> getCartsByStatus(@PathVariable ShoppingCart.Status status) {
        List<ShoppingCart> carts = shoppingCartRepository.findByStatus(status);
        return ResponseEntity.ok(carts);
    }

    //----------
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
