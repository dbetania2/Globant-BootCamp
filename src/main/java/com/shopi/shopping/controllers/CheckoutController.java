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
    public String checkout(@RequestParam Long cartId, Model model) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        // Call the service to view the cart details
        shoppingCartServices.viewCartDetails(cart);

        // Add the cart products to the model
        model.addAttribute("cartItems", cart.getProducts());
        model.addAttribute("cartId", cartId);
        return "checkout"; // Name of the checkout.html file
    }

    @PostMapping("/cart/{cartId}/buy")
    public String completeCheckout(@PathVariable Long cartId, RedirectAttributes redirectAttributes) {
        // Retrieve the cart
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        // Process the purchase
        shoppingCartServices.buyProducts(cart);

        // Send success message
        redirectAttributes.addFlashAttribute("statusMessage", "Order successfully placed!");

        // Redirect to the checkout page with the updated cart
        return "redirect:/cart/checkout?cartId=" + cartId; // Make sure this route is correct
    }
}

