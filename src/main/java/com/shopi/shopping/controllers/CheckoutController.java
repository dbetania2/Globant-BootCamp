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
    private ShoppingCartServices shoppingCartServices; // Inyectar el servicio
    @Autowired
    private ShoppingCartRepository shoppingCartRepository; // Inyectar el repositorio

    @GetMapping("/cart/checkout")
    public String checkout(@RequestParam Long cartId, @RequestParam Long customerId, Model model) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        // Agregar los productos del carrito al modelo
        model.addAttribute("cartItems", cart.getProducts());
        model.addAttribute("cartId", cartId);
        model.addAttribute("customerId", customerId); // Añadir customerId al modelo

        return "checkout"; // Nombre del archivo checkout.html
    }

    @PostMapping("/cart/{cartId}/buy")
    public String completeCheckout(@PathVariable Long cartId, @RequestParam Long customerId, RedirectAttributes redirectAttributes) {
        // Recuperar el carrito
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        // Procesar la compra
        shoppingCartServices.buyProducts(cart);

        // Enviar mensaje de éxito
        redirectAttributes.addFlashAttribute("statusMessage", "Order successfully placed!");

        // Redirigir a la página de checkout con el carrito actualizado
        return "redirect:/cart/checkout?cartId=" + cartId + "&customerId=" + customerId; // Incluir customerId
    }
}

