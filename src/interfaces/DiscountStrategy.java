package interfaces;

import models.Discount;
import models.products.Product;

public interface DiscountStrategy {
    double applyProductDiscount(Product product, Discount discount);
}
