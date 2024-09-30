package main.java.com.shopi.shopping.interfaces;
import main.java.com.shopi.shopping.models.Discount;
import main.java.com.shopi.shopping.models.products.Product;

public interface DiscountStrategy {
    double applyProductDiscount(Product product, Discount discount);
}