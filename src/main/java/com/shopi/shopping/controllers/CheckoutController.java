package com.shopi.shopping.controllers;
import com.shopi.shopping.models.ShoppingCart;
import com.shopi.shopping.repositories.ShoppingCartRepository;
import com.shopi.shopping.services.ShoppingCartServices;
import com.shopi.shopping.services.facades.ShoppingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class CheckoutController {

    @Autowired
    private ShoppingCartServices shoppingCartServices; // Inject the service
    @Autowired
    private ShoppingCartRepository shoppingCartRepository; // Inject the repository

    @GetMapping("/cart/checkout")
    public String checkout(@RequestParam Long cartId, @RequestParam Long customerId, Model model) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        // Add the products in the cart to the model
        model.addAttribute("cartItems", cart.getProducts());
        model.addAttribute("cartId", cartId);
        model.addAttribute("customerId", customerId); // Add customerId to the model

        return "checkout"; // Name of the checkout.html file
    }

    @PostMapping("/cart/{cartId}/buy")
    public String completeCheckout(@PathVariable Long cartId, @RequestParam Long customerId, RedirectAttributes redirectAttributes) {
        // Retrieve the cart
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        // Process the purchase
        shoppingCartServices.buyProducts(cart);

        // Send success message
        redirectAttributes.addFlashAttribute("statusMessage", "Order successfully placed!");

        // Redirect to the checkout page with the updated cart
        return "redirect:/cart/checkout?cartId=" + cartId + "&customerId=" + customerId; // Include customerId
    }

}

