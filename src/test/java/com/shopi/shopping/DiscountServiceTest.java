package com.shopi.shopping;
import com.shopi.shopping.factories.OrderFactory;
import com.shopi.shopping.factories.ProductFactory;
import com.shopi.shopping.models.Discount;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.services.DiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.shopi.shopping.models.Order;
import org.mockito.Mockito;

import java.util.ArrayList;

public class DiscountServiceTest {

    private DiscountService discountService;
    private OrderFactory orderFactory;

    @BeforeEach
    public void setUp() {
        discountService = new DiscountService();
        orderFactory = new OrderFactory();
    }

    @Test
    public void testApplyProductDiscount_ValidDiscount() {
        // Arrange
        Product product = ProductFactory.createProduct("CLOTHING", "Test Clothing Product", 100.0);
        Discount validDiscount = new Discount(0.1, "CLOTHING", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1)); // 10% descuento

        // Act
        double discountedPrice = discountService.applyProductDiscount(product, validDiscount);

        // Assert
        assertEquals(90.0, discountedPrice, 0.001, "Discounted price should be 90.0");
    }

    @Test
    public void testApplyProductDiscount_InvalidDiscount() {
        // Arrange
        Product product = ProductFactory.createProduct("CLOTHING", "Test Clothing Product", 100.0);
        Discount invalidDiscount = new Discount(0.1, "CLOTHING", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)); // Descuento inválido

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            discountService.applyProductDiscount(product, invalidDiscount);
        });
        assertEquals("The discount is not valid for the current date.", exception.getMessage());
    }

    @Test
    public void testApplyFirstPurchaseDiscount_True() {
        // Arrange
        double originalTotal = 100.0;
        boolean isFirstPurchase = true;
        Order order = orderFactory.createOrder(new ArrayList<>()); // Crear una orden con productos vacía
        order.setTotalAmount(originalTotal);

        // Act
        double newTotal = discountService.applyFirstPurchaseDiscount(order, isFirstPurchase);

        // Assert
        assertEquals(90.0, newTotal, 0.001, "New total after first purchase discount should be 90.0");
        assertEquals(90.0, order.getTotalAmount(), 0.001, "Order total should be updated to 90.0");
        assertEquals(1, order.getAppliedDiscounts().size(), "One discount should be applied");
        Discount appliedDiscount = order.getAppliedDiscounts().get(0);
        assertEquals(0.10, appliedDiscount.getRate(), 0.001, "Discount rate should be 0.10");
        assertEquals("First Purchase Discount", appliedDiscount.getCategory(), "Discount category should be 'First Purchase Discount'");
    }

    @Test
    public void testApplyFirstPurchaseDiscount_False() {
        // Arrange
        double originalTotal = 100.0;
        boolean isFirstPurchase = false;
        Order order = orderFactory.createOrder(new ArrayList<>());
        order.setTotalAmount(originalTotal);

        // Act
        double newTotal = discountService.applyFirstPurchaseDiscount(order, isFirstPurchase);

        // Assert
        assertEquals(100.0, newTotal, 0.001, "Total should remain unchanged if it's not the first purchase.");
        assertEquals(0, order.getAppliedDiscounts().size(), "No discounts should be applied");
    }

    @Test
    public void testApplyDiscounts_ValidDiscounts() {
        // Arrange
        double originalTotal = 100.0;
        Order order = orderFactory.createOrder(new ArrayList<>());
        order.setTotalAmount(originalTotal);

        List<Discount> discounts = new ArrayList<>();
        discounts.add(new Discount(0.1, "CLOTHING", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1))); // 10% descuento válido
        discounts.add(new Discount(0.2, "ELECTRONICS", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1))); // 20% descuento válido

        // Act
        double finalTotal = discountService.applyDiscounts(order, discounts);

        // Assert
        // Cálculo esperado:
        // 100.0 - 10% = 90.0
        // 90.0 - 20% = 72.0
        assertEquals(72.0, finalTotal, 0.001, "Final total after applying discounts should be 72.0");
        assertEquals(72.0, order.getTotalAmount(), 0.001, "Order total should be updated to 72.0");
        assertEquals(2, order.getAppliedDiscounts().size(), "Two discounts should be applied");
        assertEquals(0.1, order.getAppliedDiscounts().get(0).getRate(), 0.001, "First discount rate should be 0.1");
        assertEquals(0.2, order.getAppliedDiscounts().get(1).getRate(), 0.001, "Second discount rate should be 0.2");
    }

    @Test
    public void testApplyDiscounts_InvalidDiscounts() {
        // Arrange
        double originalTotal = 100.0;
        Order order = orderFactory.createOrder(new ArrayList<>());
        order.setTotalAmount(originalTotal);

        List<Discount> discounts = new ArrayList<>();
        discounts.add(new Discount(0.1, "CLOTHING", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2))); // Descuento inválido
        discounts.add(new Discount(0.2, "ELECTRONICS", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1))); // 20% descuento válido

        // Act
        double finalTotal = discountService.applyDiscounts(order, discounts);

        // Assert
        // Solo se aplica el 20% descuento válido
        // 100.0 - 20% = 80.0
        assertEquals(80.0, finalTotal, 0.001, "Final total after applying valid discounts should be 80.0");
        assertEquals(80.0, order.getTotalAmount(), 0.001, "Order total should be updated to 80.0");
        assertEquals(1, order.getAppliedDiscounts().size(), "Only one discount should be applied");
        assertEquals(0.2, order.getAppliedDiscounts().get(0).getRate(), 0.001, "Discount rate should be 0.2");
    }


    @Test
    public void testCalculateDiscount() {
        // Arrange
        double originalTotal = 100.0;
        Order order = orderFactory.createOrder(new ArrayList<>());
        order.setTotalAmount(originalTotal);
        Discount discount = new Discount(0.2, "Test Discount", LocalDate.now(), LocalDate.now().plusDays(1)); // 20% descuento

        // Act
        double discountAmount = discountService.calculateDiscount(order, discount);

        // Assert
        assertEquals(20.0, discountAmount, 0.001, "Discount amount should be 20.0");
    }

    @Test
    public void testAddDiscount() {
        // Arrange
        Order order = orderFactory.createOrder(new ArrayList<>());
        Discount discount = new Discount(0.1, "Test Discount", LocalDate.now(), LocalDate.now().plusDays(1)); // 10% descuento

        // Act
        discountService.addDiscount(order, discount);

        // Assert
        assertTrue(order.getAppliedDiscounts().contains(discount), "Discount should be added to the order's applied discounts");
        assertEquals(1, order.getAppliedDiscounts().size(), "One discount should be applied");
    }
}