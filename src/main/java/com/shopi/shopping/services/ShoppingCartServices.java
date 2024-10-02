package com.shopi.shopping.services;

import com.shopi.shopping.factories.OrderFactory;
import com.shopi.shopping.models.Order;
import com.shopi.shopping.interfaces.ShoppingCartInterface;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.models.ShoppingCart;
import  com.shopi.shopping.services.DiscountService;

import java.util.Comparator;


public class ShoppingCartServices implements ShoppingCartInterface {

    private OrderFactory orderFactory;
    private DiscountService discountService;

    // Constructor with dependency injection
    public ShoppingCartServices(OrderFactory orderFactory, DiscountService discountService) {
        this.orderFactory = orderFactory;
        this.discountService = discountService;
    }


    // Checkout process to create an order and apply discounts if necessary
    public Order checkout(ShoppingCart cart, boolean isFirstPurchase) {
        // Create a new order using the factory
        Order order = orderFactory.createOrder(cart.getProducts());

        // Apply discounts if applicable
        discountService.applyFirstPurchaseDiscount(order, isFirstPurchase);

        // Return the final order with the calculated total
        return order;
    }


    // Filter and display products with price > 100 from the LIBRARY category
    public void printLibraryProductsWithPriceOver100(ShoppingCart cart) {
        System.out.println("Products in the LIBRARY category with price > 100:");
        cart.getProducts().stream()
                .filter(product -> product.getPrice() > 100 && product.getType().equals("LIBRARY"))
                .forEach(System.out::println);
    }
    // Calculate and display the total sum of the prices of products from the ELECTRONICS category
    public double calculateTotalPriceForElectronicProducts(ShoppingCart cart) {
        return cart.getProducts().stream()
                .filter(product -> product.getType().equals("ELECTRONIC"))
                .mapToDouble(Product::getPrice)
                .sum();
    }
    // Calculate and display the total sum of the prices of all products
    public double calculateTotalPrice(ShoppingCart cart) {
        return cart.getProducts().stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }



    // Print all shopping cart information sorted by price
    public void printCartInfoSortedByPrice(ShoppingCart cart) {
        System.out.printf("%-10s | %-3s | %-15s | %-20s | %-5s\n", "ID Cart", "ID", "TYPE", "NAME", "PRICE");
        System.out.println("-----------------------------------------------------------------------");

        cart.getProducts().stream()
                .sorted(Comparator.comparingDouble(Product::getPrice))
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
        System.out.println("Products in the cart:");
        cart.getProducts().forEach(product ->
                System.out.printf("%-3s | %-20s | %.2f\n",
                        product.getId(),
                        product.getName(),
                        product.getPrice()));
    }

    // View details of the products in the cart (with description)
    @Override
    public void viewCartDetails(ShoppingCart cart) {
        System.out.println("Details of products in the cart:");
        cart.getProducts().forEach(product ->
                System.out.printf("%-3s | %-20s | %.2f | %s\n",
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getDescription()));
    }

    @Override
    public void addProductToCart(ShoppingCart cart, Product product) {
        if (cart != null && product != null) {
            // Add the product to the cart's product list
            cart.getProducts().add(product);
            System.out.println("Product added to the cart: " + product.getName());
        } else {
            System.out.println("Cart or product is null.");
        }
    }

    @Override
    public void buyProducts(ShoppingCart cart) {
        // Proceed with buying the products in the cart
    }


}
