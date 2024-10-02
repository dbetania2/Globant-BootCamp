package com.shopi.shopping.interfaces;
import com.shopi.shopping.models.Discount;
import com.shopi.shopping.models.products.Product;

public interface DiscountStrategy {
    double applyProductDiscount(Product product, Discount discount);
}