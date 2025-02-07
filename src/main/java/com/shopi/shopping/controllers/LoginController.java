package com.shopi.shopping.controllers;
import ch.qos.logback.core.model.Model;
import com.shopi.shopping.models.Customer;
import com.shopi.shopping.models.ShoppingCart;
import com.shopi.shopping.repositories.ShoppingCartRepository;
import com.shopi.shopping.services.CustomerService;
import com.shopi.shopping.services.ShoppingCartServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private CustomerService customerService; // Asegúrate de tener acceso a tu servicio de clientes
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Operation(summary = "Get login page", description = "Retrieves the login page for user authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login page retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping("/login")
    public String login() {
        return "login"; // Returns the name of the view (login.html)
    }

    // Método para manejar la creación del Customer
    // Método para manejar la creación del Customer
    @PostMapping("/createUser")
    public String createUser(@RequestParam String name, @RequestParam String lastName, @RequestParam String email, Model model) {
        // Crear el nuevo Customer utilizando el método del servicio
        Customer createdCustomer = customerService.createCustomerWithCart(name, lastName, email);

        // Asegúrate de que el cliente fue creado y tiene un ID
        if (createdCustomer == null || createdCustomer.getId() == null) {
            // Manejar el caso en que el cliente no se creó correctamente
            return "errorPage"; // Redirigir a una página de error
        }

        // Buscar la lista de carritos asociados al cliente
        List<ShoppingCart> carts = shoppingCartRepository.findByCustomerId(createdCustomer.getId());

        ShoppingCart newCart;
        if (carts.isEmpty()) {
            // Si no hay carritos, crea uno nuevo
            newCart = new ShoppingCart();
            newCart.setCustomer(createdCustomer);
            shoppingCartRepository.save(newCart); // Guarda el nuevo carrito
        } else {
            // Si existe al menos un carrito, toma el primero de la lista
            newCart = carts.get(0);
        }

        // Redirigir a la página de inicio con los IDs del customer y el carrito
        return "redirect:/home?customerId=" + createdCustomer.getId() + "&cartId=" + newCart.getId();
    }






}

