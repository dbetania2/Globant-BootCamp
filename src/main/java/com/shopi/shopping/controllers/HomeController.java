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
        if (customerId == null) {
            customerId = 1L; // ID predeterminado del cliente
        }
        if (cartId == null) {
            cartId = 1L; // ID predeterminado del carrito
        }

        Customer customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setCustomer(customer);
                    return shoppingCartRepository.save(newCart);
                });

        model.addAttribute("products", productService.getAllProducts()); // Carga todos los productos disponibles
        model.addAttribute("cartItems", cart.getProducts()); // Muestra los productos actuales en el carrito
        model.addAttribute("cartId", cart.getId());

        return "home";
    }


    @PostMapping("/cart/buy")
    public String checkout(@RequestParam Long cartId) {
        // Recuperar el carrito por ID
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        // Lógica de compra
        shoppingCartServices.checkout(cart, true);

        // Redirigir a la página de checkout, asegurándote de pasar el cartId
        return "redirect:/cart/checkout?cartId=" + cartId; // Cambiado a redirigir al checkout con el cartId
    }


    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, @RequestParam Long cartId, RedirectAttributes redirectAttributes) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        Product product = productService.getProductById(productId);

        boolean added = shoppingCartServices.addProductToCart(cart, product);

        shoppingCartRepository.save(cart); // Guardar el carrito después de agregar el producto

        // Imprimir el contenido del carrito en logs para verificar
        System.out.println("Cart ID: " + cartId + " Products in cart: " + cart.getProducts());

        if (added) {
            redirectAttributes.addFlashAttribute("success", "Product added to cart!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Product already in cart!");
        }

        return "redirect:/home?cartId=" + cartId; // Asegúrate de que cartId tenga un valor válido
    }



    // Método para mostrar el carrito
    @GetMapping("/cart/view")
    public String viewCart(@RequestParam Long cartId, Model model) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        model.addAttribute("cartItems", cart.getProducts());
        model.addAttribute("cartId", cart.getId());
        return "cart/view"; // Retorna la vista del carrito
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long productId, @RequestParam Long cartId, RedirectAttributes redirectAttributes) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        Product product = productService.getProductById(productId);

        // Eliminar el producto del carrito
        if (cart.getProducts().remove(product)) {
            shoppingCartRepository.save(cart);
            redirectAttributes.addFlashAttribute("success", "Product removed from cart!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Product not found in cart!");
        }

        return "redirect:/home?cartId=" + cartId;
    }



}
