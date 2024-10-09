package com.shopi.shopping.services;
import com.shopi.shopping.factories.OrderFactory;
import com.shopi.shopping.models.Order;
import com.shopi.shopping.interfaces.ShoppingCartInterface;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.models.ShoppingCart;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.shopi.shopping.repositories.ShoppingCartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service // Indicates that this class is a service managed by Spring
public class ShoppingCartServices implements ShoppingCartInterface {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartServices.class); //logger------------
    private final ShoppingCartRepository shoppingCartRepository; // Repository for shopping cart
    private final OrderFactory orderFactory;
    private final DiscountService discountService;

    // Constructor with dependency injection
    @Autowired // Automatic injection of ShoppingCartRepository, OrderFactory, and DiscountService
    public ShoppingCartServices(ShoppingCartRepository shoppingCartRepository,
                                OrderFactory orderFactory,
                                DiscountService discountService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderFactory = orderFactory;
        this.discountService = discountService;
        logger.info("ShoppingCartServices initialized with ShoppingCartRepository, OrderFactory, and DiscountService."); //logger------------
    }

    // Checkout process to create an order and apply discounts if necessary
    public Order checkout(ShoppingCart cart, boolean isFirstPurchase) {
        logger.info("Checkout process started for cart ID: {}", cart.getId()); //logger------------

        // Create a new order using the factory
        Order order = orderFactory.createOrder(cart.getProducts());

        logger.info("Order created with ID: {}", order.getId()); //logger------------

        // Apply discounts if applicable
        discountService.applyFirstPurchaseDiscount(order, isFirstPurchase);
        logger.info("Discount applied for first purchase: {}", isFirstPurchase); //logger------------

        // Return the final order with the calculated total
        return order;
    }

    // Calculate and display the total sum of the prices of all products
    public double calculateTotalPrice(ShoppingCart cart) {
        logger.info("Calculating total price for all products in cart ID: {}", cart.getId()); //logger------------

        double total = cart.getProducts().stream()
                .mapToDouble(Product::getPrice)
                .sum();

        logger.info("Total price for all products: {}", total); //logger------------
        return total;
    }

    // Print all shopping cart information sorted by price
    public void printCartInfoSortedByPrice(ShoppingCart cart) {
        System.out.printf("%-10s | %-3s | %-15s | %-20s | %-5s\n", "ID Cart", "ID", "TYPE", "NAME", "PRICE");
        System.out.println("-----------------------------------------------------------------------");

        cart.getProducts().stream()
                .sorted(Comparator.comparingDouble(Product::getPrice))
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
        logger.info("Viewing products in cart ID: {}", cart.getId()); //logger------------

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
        logger.info("Viewing details of products in cart ID: {}", cart.getId()); //logger------------

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
            if (!cart.getProducts().contains(product)) { // Only add if not already in cart
                cart.getProducts().add(product);
                shoppingCartRepository.save(cart); // Persist the updated cart
                logger.info("Product added to the cart: {}", product.getName()); //logger------------
            } else {
                logger.warn("Product is already in the cart: {}", product.getName()); //logger------------
            }
        } else {
            logger.warn("Cart or product is null."); //logger------------
        }
    }

    // Remove product from cart
    public void removeProductFromCart(ShoppingCart cart, Product product) {
        if (cart != null && product != null) {
            if (cart.getProducts().remove(product)) {
                shoppingCartRepository.save(cart); // Persist the updated cart
                logger.info("Product removed from the cart: {}", product.getName()); //logger------------
            } else {
                logger.warn("Product not found in the cart: {}", product.getName()); //logger------------
            }
        } else {
            logger.warn("Cart or product is null."); //logger------------
        }
    }

    // Method to get a shopping cart by ID
    public Optional<ShoppingCart> getCartById(Long id) {
        logger.info("Fetching cart with ID: {}", id); //logger------------
        return shoppingCartRepository.findById(id);
    }

    // Method to get all shopping carts
    public List<ShoppingCart> getAllCarts() {
        logger.info("Fetching all shopping carts."); //logger------------
        return shoppingCartRepository.findAll();
    }

    // Method to delete a shopping cart by ID
    public void deleteCartById(Long id) {
        logger.info("Deleting cart with ID: {}", id); //logger------------
        shoppingCartRepository.deleteById(id);
    }

    // Method to get all carts for a specific customer
    public List<ShoppingCart> getCartsByCustomerId(Long customerId) {
        logger.info("Fetching carts for customer ID: {}", customerId); //logger------------
        return shoppingCartRepository.findByCustomerId(customerId);
    }

    // Method to get all carts with a specific status
    public List<ShoppingCart> getCartsByStatus(ShoppingCart.Status status) {
        logger.info("Fetching carts with status: {}", status); //logger------------
        return shoppingCartRepository.findByStatus(status);
    }

    @Override
    public void buyProducts(ShoppingCart cart) {
        // Proceed with buying the products in the cart
        logger.info("Buying products in cart ID: {}", cart.getId()); //logger------------
    }
}