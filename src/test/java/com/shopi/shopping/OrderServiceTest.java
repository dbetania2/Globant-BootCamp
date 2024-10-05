package com.shopi.shopping;
import com.shopi.shopping.factories.ProductFactory;
import com.shopi.shopping.models.Discount;
import com.shopi.shopping.models.StandardOrder;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.services.DiscountService;
import com.shopi.shopping.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class OrderServiceTest {

    private DiscountService discountService;
    private OrderService orderService;
    private StandardOrder order;

    @BeforeEach
    public void setUp() {
        discountService = Mockito.mock(DiscountService.class);
        orderService = new OrderService(discountService);

        // Crear productos usando el ProductFactory
        List<Product> products = new ArrayList<>();
        products.add(ProductFactory.createProduct("CLOTHING", "T-Shirt", 50.0)); // Producto de ropa
        products.add(ProductFactory.createProduct("ELECTRONICS", "Headphones", 50.0)); // Producto de electrónica

        // Crear una nueva StandardOrder
        order = new StandardOrder(products);
    }
    @Test
    public void testApplyDiscounts() {
        // Arrange
        Discount discount1 = new Discount(0.1, "CLOTHING", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        Discount discount2 = new Discount(0.2, "ELECTRONICS", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));

        List<Discount> discounts = Arrays.asList(discount1, discount2);

        // Simular el comportamiento del DiscountService
        when(discountService.calculateDiscount(order, discount1)).thenReturn(5.0); // 10% de 50
        when(discountService.calculateDiscount(order, discount2)).thenReturn(10.0); // 20% de 50

        // Act
        orderService.applyDiscounts(order, discounts);

        // Assert
        assertEquals(85.0, order.getTotalAmount(), 0.001, "Total amount after discounts should be 85.0");
        assertEquals(2, order.getAppliedDiscounts().size(), "Two discounts should be applied");
    }
    @Test
    public void testAddDiscount() {
        // Arrange
        Discount discount = new Discount(0.1, "CLOTHING", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));

        // Act
        orderService.addDiscount(order, discount);

        // Assert
        assertEquals(1, order.getAppliedDiscounts().size(), "One discount should be applied");
        assertEquals(0.1, order.getAppliedDiscounts().get(0).getRate(), 0.001, "Discount rate should be 0.1");
    }

    @Test
    public void testGetDiscountSummary() {
        // Arrange
        Discount discount = new Discount(0.1, "CLOTHING", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        orderService.addDiscount(order, discount);
        order.setTotalAmount(90.0); // Set new total after applying discount

        // Act
        String summary = orderService.getDiscountSummary(order);

        // Assert
        assertTrue(summary.contains("Applied discounts:"), "Summary should contain applied discounts");
        assertTrue(summary.contains("Total after discounts: 90.0"), "Summary should reflect total after discounts");
    }
}