package main.java.com.shopi.shopping.interfaces;

import main.java.com.shopi.shopping.models.ShoppingCart;
import main.java.com.shopi.shopping.models.products.Product;

public interface ShoppingCartInterface {

    void viewCart(ShoppingCart cart);

    void addProduct(ShoppingCart cart, Product product);

    void viewCartDetails(ShoppingCart cart);

    void buyProducts(ShoppingCart cart);
}