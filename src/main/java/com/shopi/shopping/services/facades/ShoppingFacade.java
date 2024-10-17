package com.shopi.shopping.services.facades;
import com.shopi.shopping.models.Customer;
import com.shopi.shopping.models.ShoppingCart;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.services.NotificationService;
import com.shopi.shopping.services.OrderService;
import com.shopi.shopping.services.ProductService;
import com.shopi.shopping.services.ShoppingCartServices;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class ShoppingFacade {

    private final ShoppingCartServices shoppingCartService; // Servicio para operaciones del carrito
    private final ProductService productService; // Servicio para operaciones con productos
    private final OrderService orderService; // Servicio para operaciones de órdenes
    private final NotificationService notificationService; // Servicio para notificaciones

    public ShoppingFacade(ShoppingCartServices shoppingCartService, ProductService productService,
                              OrderService orderService, NotificationService notificationService) {
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
        this.orderService = orderService;
        this.notificationService = notificationService;
    }

    // Ver productos en el carrito
    public void viewCart(ShoppingCart cart) {
        if (cart == null) {
            System.out.println("El carrito es nulo. No se pueden ver los productos.");
            return;
        }
        shoppingCartService.viewCart(cart);
    }

    // Ver detalles de los productos en el carrito
    public void viewCartDetails(ShoppingCart cart) {
        if (cart == null) {
            System.out.println("El carrito es nulo. No se pueden ver los detalles de los productos.");
            return;
        }
        shoppingCartService.viewCartDetails(cart);
    }

    // Agregar producto al carrito
    public boolean addProductToCart(ShoppingCart cart, Product product) {
        return shoppingCartService.addProductToCart(cart, product);
    }

    // Remover producto del carrito
    public void removeProductFromCart(ShoppingCart cart, Product product) {
        shoppingCartService.removeProductFromCart(cart, product);
    }

    // Comprar productos
    public void buyProducts(ShoppingCart cart) {
        shoppingCartService.buyProducts(cart);
    }

    // Calcular el precio total de los productos en el carrito
    public BigDecimal calculateTotalPrice(ShoppingCart cart) {
        return shoppingCartService.calculateTotalPrice(cart);
    }

    // Obtiene el carrito actual (puedes implementar según tu lógica)



}
