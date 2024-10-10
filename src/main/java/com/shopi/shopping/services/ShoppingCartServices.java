package com.shopi.shopping.services;
import com.shopi.shopping.factories.OrderFactory;
import com.shopi.shopping.models.Order;
import com.shopi.shopping.interfaces.ShoppingCartInterface;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.models.ShoppingCart;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.shopi.shopping.repositories.ShoppingCartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ShoppingCartServices implements ShoppingCartInterface {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartServices.class);
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderFactory orderFactory;
    private final DiscountService discountService;

    @Autowired
    public ShoppingCartServices(ShoppingCartRepository shoppingCartRepository,
                                OrderFactory orderFactory,
                                DiscountService discountService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderFactory = orderFactory;
        this.discountService = discountService;
        logger.info("ShoppingCartServices initialized with ShoppingCartRepository, OrderFactory, and DiscountService.");
    }

    // Checkout process to create an order and apply discounts if necessary
    public Order checkout(ShoppingCart cart, boolean isFirstPurchase) {
        if (cart == null || cart.getProducts().isEmpty()) {
            logger.warn("Cannot proceed to checkout: Cart is null or empty.");
            throw new IllegalArgumentException("Cart is null or empty");
        }
        logger.info("Checkout process started for cart ID: {}", cart.getId());

        // Create a new order using the factory
        Order order = orderFactory.createOrder(cart.getProducts());
        logger.info("Order created with ID: {}", order.getId());

        // Apply discounts if applicable
        discountService.applyFirstPurchaseDiscount(order, isFirstPurchase);
        logger.info("Discount applied for first purchase: {}", isFirstPurchase);

        return order;
    }

    // Calculate and display the total sum of the prices of all products
    public BigDecimal calculateTotalPrice(ShoppingCart cart) {
        if (cart == null || cart.getProducts().isEmpty()) {
            logger.warn("Cannot calculate total price: Cart is null or empty.");
            return BigDecimal.ZERO; // Return 0 if cart is empty
        }
        logger.info("Calculating total price for all products in cart ID: {}", cart.getId());
        BigDecimal total = cart.getProducts().stream()
                .map(Product::getPrice) // Assuming price is already BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.info("Total price for all products: {}", total);
        return total;
    }

    // Print all shopping cart information sorted by price
    public void printCartInfoSortedByPrice(ShoppingCart cart) {
        if (cart == null) {
            logger.warn("Cart is null. Cannot print information.");
            return;
        }
        System.out.printf("%-10s | %-3s | %-15s | %-20s | %-5s\n", "ID Cart", "ID", "TYPE", "NAME", "PRICE");
        System.out.println("-----------------------------------------------------------------------");
        cart.getProducts().stream()
                .sorted(Comparator.comparing(Product::getPrice)) // Sort by price
                .forEach(product -> System.out.printf(
                        "%-10s | %-3s | %-15s | %-20s | %.2f\n",
                        cart.getId(),
                        product.getId(),
                        product.getType(),
                        product.getName(),
                        product.getPrice()
                ));
    }

    // View products in the cart
    @Override
    public void viewCart(ShoppingCart cart) {
        if (cart == null) {
            logger.warn("Cart is null. Cannot view products.");
            return;
        }
        logger.info("Viewing products in cart ID: {}", cart.getId());
        System.out.println("Products in the cart:");
        cart.getProducts().forEach(product ->
                System.out.printf("%-3s | %-20s | %.2f\n",
                        product.getId(),
                        product.getName(),
                        product.getPrice()));
    }

    // View details of the products in the cart (with description)
    @Override
    public void viewCartDetails(ShoppingCart cart) {
        if (cart == null) {
            logger.warn("Cart is null. Cannot view product details.");
            return;
        }
        logger.info("Viewing details of products in cart ID: {}", cart.getId());
        System.out.println("Details of products in the cart:");
        cart.getProducts().forEach(product ->
                System.out.printf("%-3s | %-20s | %.2f | %s\n",
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getDescription()));
    }

    // Add product to cart
    public void addProductToCart(ShoppingCart cart, Product product) {
        if (cart != null && product != null) {
            cart.getProducts().add(product); // Agrega el producto a la lista de productos del carrito
        }
    }


    // Remove product from cart
    public void removeProductFromCart(ShoppingCart cart, Product product) {
        if (cart == null || product == null) {
            logger.warn("Cart or product is null. Cannot remove product.");
            return;
        }
        if (cart.getProducts().remove(product)) {
            shoppingCartRepository.save(cart); // Persist the updated cart
            logger.info("Product removed from the cart: {}", product.getName());
        } else {
            logger.warn("Product not found in the cart: {}", product.getName());
        }
    }

    // Method to get a shopping cart by ID
    public Optional<ShoppingCart> getCartById(Long id) {
        logger.info("Fetching cart with ID: {}", id);
        return shoppingCartRepository.findById(id);
    }

    // Method to get all shopping carts
    public List<ShoppingCart> getAllCarts() {
        logger.info("Fetching all shopping carts.");
        return shoppingCartRepository.findAll();
    }

    // Method to delete a shopping cart by ID
    public void deleteCartById(Long id) {
        logger.info("Deleting cart with ID: {}", id);
        shoppingCartRepository.deleteById(id);
    }

    // Method to get all carts for a specific customer
    public List<ShoppingCart> getCartsByCustomerId(Long customerId) {
        logger.info("Fetching carts for customer ID: {}", customerId);
        return shoppingCartRepository.findByCustomerId(customerId);
    }

    // Method to get all carts with a specific status
    public List<ShoppingCart> getCartsByStatus(ShoppingCart.Status status) {
        logger.info("Fetching carts with status: {}", status);
        return shoppingCartRepository.findByStatus(status);
    }

    @Override
    public void buyProducts(ShoppingCart cart) {
        if (cart == null || cart.getProducts().isEmpty()) {
            logger.warn("Cannot buy products: Cart is null or empty.");
            return;
        }
        logger.info("Buying products in cart ID: {}", cart.getId());
        // Add your business logic for buying products here
    }
}
