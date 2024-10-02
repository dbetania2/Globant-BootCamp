package com.shopi.shopping;
import com.shopi.shopping.factories.OrderFactory;
import com.shopi.shopping.models.Customer;
import com.shopi.shopping.factories.ProductFactory;
import com.shopi.shopping.models.ShoppingCart;
import com.shopi.shopping.services.DiscountService;
import com.shopi.shopping.services.ShoppingCartServices;

import java.time.LocalDate;

public class ShoppingApp {
    public static void main(String[] args) {
        // Create customer using the Builder
        Customer customer = new Customer.CustomerBuilder("John", "Smith")
                .setBirthDate(LocalDate.of(1990, 1, 1))
                .setEmail("john@gmail.com")
                .setPhone("123456789")
                .setIdentificationNumber("DNI123")
                .build();
        // Create cart
        ShoppingCart cart = new ShoppingCart(customer);

        // Instantiate ShoppingCartServices
        OrderFactory orderFactory = new OrderFactory();
        DiscountService discountService = new DiscountService();
        ShoppingCartServices shoppingCartService = new ShoppingCartServices(orderFactory, discountService);


        // Add products to the cart using the addProductToCart method
        // ELECTRONIC Products
        shoppingCartService.addProductToCart(cart, ProductFactory.createProduct("ELECTRONICS", "CPU I5", 200.00));
        shoppingCartService.addProductToCart(cart, ProductFactory.createProduct("ELECTRONICS", "CPU I7", 500.40));
        shoppingCartService.addProductToCart(cart, ProductFactory.createProduct("ELECTRONICS", "GPU RTX 3060", 150.00));

        // LIBRARY Products
        shoppingCartService.addProductToCart(cart, ProductFactory.createProduct("BOOK", "Harry Potter", 15.40));
        shoppingCartService.addProductToCart(cart, ProductFactory.createProduct("BOOK", "1984", 20.30));
        shoppingCartService.addProductToCart(cart, ProductFactory.createProduct("BOOK", "Java 8", 120.00));

        // OTHER Products
        shoppingCartService.addProductToCart(cart, ProductFactory.createProduct("CLOTHING", "T-shirt", 12.40));
        shoppingCartService.addProductToCart(cart, ProductFactory.createProduct("CLOTHING", "Mug", 5.99));
        shoppingCartService.addProductToCart(cart, ProductFactory.createProduct("CLOTHING", "Backpack", 30.00));



        // Call methods
        shoppingCartService.printLibraryProductsWithPriceOver100(cart);

        System.out.println();

        double totalPrice = shoppingCartService.calculateTotalPrice(cart);
        System.out.println("Total sum of prices of all products: " + totalPrice);

        System.out.println();

        double totalElectronics = shoppingCartService.calculateTotalPriceForElectronicProducts(cart);
        System.out.println("Total sum of prices of electronic products: " + totalElectronics);

        System.out.println();

        shoppingCartService.printCartInfoSortedByPrice(cart);
    }
}
