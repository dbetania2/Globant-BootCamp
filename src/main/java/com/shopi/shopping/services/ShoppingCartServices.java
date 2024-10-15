package com.shopi.shopping.services;
import com.shopi.shopping.configuration.RabbitConfig;
import com.shopi.shopping.factories.OrderFactory;
import com.shopi.shopping.models.*;
import com.shopi.shopping.interfaces.ShoppingCartInterface;
import com.shopi.shopping.models.products.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import com.shopi.shopping.repositories.ShoppingCartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Service
public class ShoppingCartServices implements ShoppingCartInterface {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartServices.class);
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderFactory orderFactory;
    private final DiscountService discountService;
    private final OrderService orderService;
    private final NotificationService notificationService;

    @Autowired
    private RabbitTemplate rabbitTemplate;//--
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public ShoppingCartServices(ShoppingCartRepository shoppingCartRepository,
                                OrderFactory orderFactory,
                                DiscountService discountService,
                                OrderService orderService, NotificationService notificationService, RabbitTemplate rabbitTemplate, AmqpTemplate amqpTemplate) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderFactory = orderFactory;
        this.discountService = discountService;
        this.orderService = orderService;
        this.notificationService = notificationService;
        this.rabbitTemplate = rabbitTemplate;
        this.amqpTemplate = amqpTemplate;
        logger.info("ShoppingCartServices initialized with ShoppingCartRepository, OrderFactory, DiscountService, and OrderService.");
    }
    @Override
    public void buyProducts(ShoppingCart cart) {
        // Validación de entrada
        if (cart == null || cart.getProducts() == null || cart.getProducts().isEmpty()) {
            logger.warn("Cannot buy products: Cart is null or empty.");
            return;
        }

        logger.info("Buying products in cart ID: {}", cart.getId());

        // Crear el StandardOrder y guardarlo
        StandardOrder order = new StandardOrder(new ArrayList<>(cart.getProducts())); // Asegurarse de obtener una copia de la lista

        try {
            // Guardar la orden
            orderService.saveOrder(order);

            // Cambiar el estado del carrito a SUBMITTED
            cart.setStatus(ShoppingCart.Status.SUBMIT);
            shoppingCartRepository.save(cart); // Guardar el carrito actualizado

            // Crear el evento
            Event event = new Event(cart.getId(), "SUBMITTED"); // Aquí puedes cambiar el tipo de evento

            // Notificar el evento
            notificationService.notify(event); // Llamar al método notify para manejar la notificación a RabbitMQ

            logger.info("Notification sent for cart ID: {}", cart.getId());

        } catch (Exception e) {
            logger.error("Error occurred while processing the order for cart ID: {}. Error: {}", cart.getId(), e.getMessage());
        }
    }





    // Checkout process to create an order and apply discounts if necessary
    public Order checkout(ShoppingCart cart, boolean isFirstPurchase) {
        if (cart == null || cart.getProducts().isEmpty()) {
            logger.warn("Cannot proceed to checkout: Cart is null or empty.");
            throw new IllegalArgumentException("Cart is null or empty");
        }

        logger.info("Checkout process started for cart ID: {}", cart.getId());

        // Check if the necessary services are null
        if (orderFactory == null) {
            logger.error("Order factory is not initialized.");
            throw new IllegalStateException("Order factory is not initialized.");
        }

        if (discountService == null) {
            logger.error("Discount service is not initialized.");
            throw new IllegalStateException("Discount service is not initialized.");
        }

        // Create a new order using the factory
        Order order;
        try {
            order = orderFactory.createOrder(cart.getProducts());
            logger.info("Order created with ID: {}", order.getId());
        } catch (IllegalArgumentException e) {
            logger.error("Failed to create order due to invalid arguments: {}", e.getMessage());
            throw new RuntimeException("Failed to create order due to invalid arguments.", e);
        } catch (Exception e) {
            logger.error("Failed to create order: {}", e.getMessage());
            throw new RuntimeException("Failed to create order.", e);
        }

        // Apply discounts if applicable
        try {
            discountService.applyFirstPurchaseDiscount(order, isFirstPurchase);
            logger.info("Discount applied for first purchase: {}", isFirstPurchase);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to apply discount due to invalid arguments: {}", e.getMessage());
            throw new RuntimeException("Failed to apply discount due to invalid arguments.", e);
        } catch (Exception e) {
            logger.error("Failed to apply discount: {}", e.getMessage());
            throw new RuntimeException("Failed to apply discount.", e);
        }

        return order;
    }



    // Calculate and display the total sum of the prices of all products
    public BigDecimal calculateTotalPrice(ShoppingCart cart) {
        if (cart == null) {
            logger.warn("Cart is null. Cannot calculate total price.");
            return BigDecimal.ZERO; // Return zero if the cart is null
        }

        BigDecimal total = BigDecimal.ZERO; // Initialize the total
        logger.info("Calculating total price for cart ID: {}", cart.getId());

        // Check if the cart has products
        if (cart.getProducts().isEmpty()) {
            logger.warn("Cart ID {} is empty. Total price is zero.", cart.getId());
            return BigDecimal.ZERO; // Return zero if the cart is empty
        }

        // Iterate through products and sum their prices
        for (Product product : cart.getProducts()) {
            if (product.getPrice() == null) {
                logger.error("Product ID: {} - Name: {} has a null price. Cannot calculate total price.", product.getId(), product.getName());
                throw new IllegalArgumentException("Product price cannot be null.");
            }

            if (product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                logger.error("Product ID: {} - Name: {} has a negative price: {}. Cannot calculate total price.", product.getId(), product.getName(), product.getPrice());
                throw new IllegalArgumentException("Product price cannot be negative.");
            }

            logger.debug("Adding price of product ID: {} - Name: {} - Price: {}", product.getId(), product.getName(), product.getPrice());
            total = total.add(product.getPrice()); // Add the price of each product
        }

        logger.info("Total price calculated for cart ID {}: {}", cart.getId(), total);
        return total; // Return the calculated total
    }


    // Print all shopping cart information sorted by price--------------
    public void printCartInfoSortedByPrice(ShoppingCart cart) {
        if (cart == null) {
            logger.warn("Cart is null. Cannot print information.");
            return;
        }
        logger.info("Printing cart info sorted by price for cart ID: {}", cart.getId());
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
        cart.getProducts().forEach(product -> {
            logger.debug("Product in cart: ID: {} - Name: {} - Price: {}", product.getId(), product.getName(), product.getPrice());
            System.out.printf("%-3s | %-20s | %.2f\n", product.getId(), product.getName(), product.getPrice());
        });
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
        cart.getProducts().forEach(product -> {
            logger.debug("Product detail: ID: {} - Name: {} - Price: {} - Description: {}",
                    product.getId(), product.getName(), product.getPrice(), product.getDescription());
            System.out.printf("%-3s | %-20s | %.2f | %s\n",
                    product.getId(), product.getName(), product.getPrice(), product.getDescription());
        });
    }
    // Add product to cart---------------------------------

    @Override
    public boolean addProductToCart(ShoppingCart cart, Product product) {
        logger.info("Attempting to add product ID: {} - Name: {} to cart ID: {}",
                product != null ? product.getId() : "null",
                product != null ? product.getName() : "null",
                cart != null ? cart.getId() : "null");

        // Validate arguments
        if (cart == null) {
            logger.error("Failed to add product: Cart is null.");
            throw new IllegalArgumentException("Cart cannot be null.");
        }

        if (product == null) {
            logger.error("Failed to add product: Product is null.");
            throw new IllegalArgumentException("Product cannot be null.");
        }

        // Check if the product is already in the cart
        if (cart.getProducts().contains(product)) {
            logger.warn("Product already exists in the cart: ID: {} - Name: {}", product.getId(), product.getName());
            return false; // The product is already in the cart
        }

        // Add the product to the cart
        boolean added = cart.getProducts().add(product);
        logger.info("Product added: {}", added);
        return added; // Indicates that the product was successfully added
    }





    // Remove product from cart
    @Override
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


    // Public method without cache------
    public Optional<ShoppingCart> getCartById(Long id) {
        logger.info("Fetching cart with ID: {}", id);
        return shoppingCartRepository.findById(id);
    }

    // Public method with cache------
    @Cacheable(value = "shoppingCarts", key = "#id")
    public Optional<ShoppingCart> getCartByIdWithCache(Long id) {
        return getCartById(id);
    }

    // Public method without cache------
    public void deleteCartById(Long id) {
        logger.info("Deleting cart with ID: {}", id);
        shoppingCartRepository.deleteById(id);
    }

    // Public method with cache------
    @CacheEvict(value = "shoppingCarts", key = "#id")
    public void deleteCartByIdWithCache(Long id) {
        deleteCartById(id); // Llamar al método sin caché
    }

    // Public method without cache------
    public List<ShoppingCart> getCartsByStatus(ShoppingCart.Status status) {
        logger.info("Fetching carts with status: {}", status);
        return shoppingCartRepository.findByStatus(status);
    }

    // Public method with cache------
    @Cacheable(value = "shoppingCartsByStatus", key = "#status")
    public List<ShoppingCart> getCartsByStatusWithCache(ShoppingCart.Status status) {
        return shoppingCartRepository.findByStatus(status);
    }


    // Method to get all shopping carts
    public List<ShoppingCart> getAllCarts() {
        logger.info("Fetching all shopping carts.");
        return shoppingCartRepository.findAll();
    }
    // Method to get all carts for a specific customer
    public List<ShoppingCart> getCartsByCustomerId(Long customerId) {
        logger.info("Fetching carts for customer ID: {}", customerId);
        return shoppingCartRepository.findByCustomerId(customerId);
    }


}
