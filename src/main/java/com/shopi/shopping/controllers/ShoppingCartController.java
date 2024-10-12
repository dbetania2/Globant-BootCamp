package com.shopi.shopping.controllers;
import com.shopi.shopping.models.ShoppingCart;
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
}
