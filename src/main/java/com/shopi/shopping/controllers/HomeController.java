package com.shopi.shopping.controllers;
import com.shopi.shopping.models.Customer;
import com.shopi.shopping.models.ShoppingCart;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.repositories.ShoppingCartRepository;
import com.shopi.shopping.services.CustomerService;
import com.shopi.shopping.services.ProductService;
import com.shopi.shopping.services.ShoppingCartServices;
import com.shopi.shopping.services.facades.ShoppingFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShoppingCartServices shoppingCartServices;

    @GetMapping("/home")
    public String showHomePage(Model model, @RequestParam(required = false) Long cartId, @RequestParam(required = false) Long customerId) {
        // Validate customerId
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }

        // Fetch the Customer
        Customer customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        // Fetch the ShoppingCart
        ShoppingCart cart;
        if (cartId != null) {
            cart = shoppingCartRepository.findById(cartId)
                    .orElseGet(() -> {
                        // If the cart is not found, create a new one
                        ShoppingCart newCart = new ShoppingCart();
                        newCart.setCustomer(customer);
                        return shoppingCartRepository.save(newCart);
                    });
        } else {
            // If cartId is not provided, automatically create a new one
            cart = new ShoppingCart();
            cart.setCustomer(customer);
            cart = shoppingCartRepository.save(cart);
        }

        // Add attributes to the model
        model.addAttribute("products", productService.getAllProducts()); // Load all available products
        model.addAttribute("cartItems", cart.getProducts()); // Show the current products in the cart
        model.addAttribute("cartId", cart.getId());
        model.addAttribute("customerId", customerId); // Add customerId to the model

        return "home";
    }

    @PostMapping("/cart/buy")
    public String checkout(@RequestParam Long cartId, @RequestParam Long customerId) {
        // Retrieve the cart by ID
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        // Checkout logic
        shoppingCartServices.checkout(cart, true);

        return "redirect:/cart/checkout?cartId=" + cartId + "&customerId=" + customerId;
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, @RequestParam Long cartId, @RequestParam Long customerId, RedirectAttributes redirectAttributes) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        Product product = productService.getProductById(productId);

        boolean added = shoppingCartServices.addProductToCart(cart, product);

        shoppingCartRepository.save(cart); // Save the cart after adding the product

        // Print the cart contents to logs for verification
        System.out.println("Cart ID: " + cartId + " Products in cart: " + cart.getProducts());

        if (added) {
            redirectAttributes.addFlashAttribute("success", "Product added to cart!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Product already in cart!");
        }

        return "redirect:/home?cartId=" + cartId + "&customerId=" + customerId;
    }

    // Method to view the cart
    @GetMapping("/cart/view")
    public String viewCart(@RequestParam Long cartId, Model model) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        model.addAttribute("cartItems", cart.getProducts());
        model.addAttribute("cartId", cart.getId());
        return "cart/view"; // Returns the cart view
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long productId,
                                 @RequestParam Long cartId,
                                 @RequestParam Long customerId,
                                 RedirectAttributes redirectAttributes) {
        // Log received parameters for debugging
        System.out.println("Removing product with ID: " + productId +
                " from cart ID: " + cartId +
                " for customer ID: " + customerId);

        // Retrieve the shopping cart by ID
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        // Retrieve the product by ID
        Product product = productService.getProductById(productId);

        // Remove the product from the cart using the service method
        shoppingCartServices.removeProductFromCart(cart, product);

        // Check the result of the removal operation
        if (cart.getProducts().contains(product)) {
            redirectAttributes.addFlashAttribute("error", "Product not found in cart!");
        } else {
            redirectAttributes.addFlashAttribute("success", "Product removed from cart!");
        }

        // Redirect to the home page with the cartId and customerId
        return "redirect:/home?cartId=" + cartId + "&customerId=" + customerId;
    }
}