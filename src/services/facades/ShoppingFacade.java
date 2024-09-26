package services.facades;

import models.Customer;
import models.products.Product;
import models.ShoppingCart;
import services.ShoppingCartServices;

public class ShoppingFacade {
    private Customer customer;  // Customer associated with the shopping session
    private ShoppingCart shoppingCart;  // The customer's shopping cart
    private ShoppingCartServices shoppingCartService;  // Service for handling cart operations


    // Constructor initializing the facade with a customer and shopping cart service
    public ShoppingFacade(Customer customer, ShoppingCartServices shoppingCartService) {
        this.customer = customer;
        this.shoppingCart = new ShoppingCart(customer);  // Create a shopping cart for the customer
        this.shoppingCartService = shoppingCartService;  // Assign the cart service
    }

    // Adds a product to the cart by delegating to the service
    public void addProductToCart(Product product) {
        shoppingCartService.addProduct(shoppingCart, product);
    }

    // Handles the checkout process for completing the purchase
    public void checkout() {
        if (shoppingCart.getProducts().isEmpty()) {
            System.out.println("Cart is empty, cannot proceed with checkout.");
        } else {
            System.out.println("Processing checkout for " + customer.getName());
            shoppingCartService.buyProducts(shoppingCart);  // Delegates the checkout process to the service
        }
    }

    // Displays the details of the products in the shopping cart by calling the service method
    public void viewCartDetails() {
        shoppingCartService.viewCartDetails(shoppingCart);  // Delegates the logic to the service
    }

    // Returns the shopping cart associated with this facade
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }
}
