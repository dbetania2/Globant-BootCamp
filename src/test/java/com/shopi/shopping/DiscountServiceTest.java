package com.shopi.shopping;
import com.shopi.shopping.factories.OrderFactory;
import com.shopi.shopping.factories.ProductFactory;
import com.shopi.shopping.models.Discount;
import com.shopi.shopping.models.StandardOrder;
import com.shopi.shopping.models.products.Clothing;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.repositories.DiscountRepository;
import com.shopi.shopping.services.DiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import com.shopi.shopping.models.Order;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
class DiscountServiceTest {

    @InjectMocks
    private DiscountService discountService;

    @Mock
    private DiscountRepository discountRepository; // Mock if necessary

    private Order order;
    private Product tShirt; // Now of type Product
    private Discount discount;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Use ProductFactory to create a product
        tShirt = ProductFactory.createProduct("CLOTHING", "T-Shirt", 50.0);

        // Create an order with the product
        order = new StandardOrder(Arrays.asList(tShirt));
        order.setTotalAmount(100.0); // Set the initial total amount of the order

        // Create a discount (10% discount) with all necessary parameters
        discount = new Discount(0.10, "CLOTHING", "Percentage", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
    }

    @Test
    public void testApplyDiscounts_ValidDiscounts() {
        // Apply the discount to the order
        discountService.applyDiscounts(order, Arrays.asList(discount));

        // Verify that the total is 90.0 after applying the 10% discount
        assertEquals(90.0, order.getTotalAmount(), "Total amount should be updated to 90.0 after applying discount");
        assertEquals(1, order.getAppliedDiscounts().size(), "One discount should be applied");
        assertTrue(order.getAppliedDiscounts().contains(discount), "Applied discounts should include the 10% discount");
    }

    @Test
    public void testApplyFirstPurchaseDiscount_Valid() {
        // Apply first purchase discount
        discountService.applyFirstPurchaseDiscount(order, true); // Simulate that it's the first purchase

        // Expected total after applying the discount
        double expectedTotal = 90.0; // 100 - (10% of 100)

        assertEquals(expectedTotal, order.getTotalAmount(), "Total amount should be updated to 90.0 after applying first purchase discount");
    }

    @Test
    public void testIsDiscountValid_Invalid() {
        discount.setEndDate(LocalDate.now().minusDays(1)); // Make the discount invalid
        assertFalse(discountService.isDiscountValid(discount), "Discount should be invalid after its end date");
    }

    @Test
    public void testIsDiscountValid_Valid() {
        assertTrue(discountService.isDiscountValid(discount), "Discount should be valid");
    }

    @Test
    public void testApplyProductDiscount_InvalidDiscount() {
        // Create a clothing type product
        Product invalidTShirt = new Clothing(50.0, "T-Shirt", "zzzzz");

        // Create a discount that will be invalid in the business logic
        Discount invalidDiscount = new Discount(0.1, "CLOTHING", "Percentage",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(5)); // Discount starting in the future

        // Verify if the discount is valid before applying it
        assertFalse(discountService.isDiscountValidForOrder(order, invalidDiscount), "Invalid discount should not be valid for the order");

        // Attempt to apply the invalid discount
        double finalPrice = discountService.applyProductDiscount(invalidTShirt, invalidDiscount);

        // Verify that the final price remains 50.0
        assertEquals(50.0, finalPrice, "Price of T-Shirt should remain 50.0 when discount is invalid");
    }
}
