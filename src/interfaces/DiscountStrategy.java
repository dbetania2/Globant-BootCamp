package interfaces;

import models.products.Discount;
import models.products.Product;

public interface DiscountStrategy {
    double applyProductDiscount(Product product, Discount discount);
}
