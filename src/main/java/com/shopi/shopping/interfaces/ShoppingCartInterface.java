package com.shopi.shopping.interfaces;

import com.shopi.shopping.models.ShoppingCart;
import com.shopi.shopping.models.products.Product;


public interface ShoppingCartInterface {

    void viewCart(ShoppingCart cart);

    boolean addProductToCart(ShoppingCart cart, Product product);

    void removeProductFromCart(ShoppingCart cart, Product product);
    void viewCartDetails(ShoppingCart cart);

    void buyProducts(ShoppingCart cart);

}