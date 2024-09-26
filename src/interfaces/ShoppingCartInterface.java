package interfaces;

import models.products.Product;
import models.ShoppingCart;

public interface ShoppingCartInterface {

    void viewCart(ShoppingCart cart);

    void addProduct(ShoppingCart cart, Product product);

    void viewCartDetails(ShoppingCart cart);

    void buyProducts(ShoppingCart cart);
}

