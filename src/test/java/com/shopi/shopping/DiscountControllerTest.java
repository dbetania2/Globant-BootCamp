package com.shopi.shopping;
import com.shopi.shopping.controllers.DiscountController;
import com.shopi.shopping.models.Discount;
import com.shopi.shopping.services.DiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class DiscountControllerTest {

    @InjectMocks
    private DiscountController discountController; // Controller to be tested

    @Mock
    private DiscountService discountService; // Mocked service to isolate the controller tests

    private Discount discount; // Discount object for testing

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        discount = new Discount(); // Create a new Discount object for tests
        discount.setId(1L);
        discount.setCategory("Test Category");
        // Add other fields as necessary
    }

    // Test case for creating a discount
    @Test
    public void testCreateDiscount() {
        when(discountService.createDiscount(any(Discount.class))).thenReturn(discount); // Mock service call

        ResponseEntity<Discount> response = discountController.createDiscount(discount); // Call the controller method

        assertEquals(HttpStatus.CREATED, response.getStatusCode()); // Assert status code
        assertEquals(discount, response.getBody()); // Assert response body
        verify(discountService, times(1)).createDiscount(any(Discount.class)); // Verify service interaction
    }

    // Test case for updating a discount
    @Test
    public void testUpdateDiscount() {
        when(discountService.updateDiscount(any(Discount.class))).thenReturn(discount); // Mock service call

        ResponseEntity<Discount> response = discountController.updateDiscount(1L, discount); // Call the controller method

        assertEquals(HttpStatus.OK, response.getStatusCode()); // Assert status code
        assertEquals(discount, response.getBody()); // Assert response body
        verify(discountService, times(1)).updateDiscount(any(Discount.class)); // Verify service interaction
    }

    // Test case for deleting a discount
    @Test
    public void testDeleteDiscount() {
        ResponseEntity<Void> response = discountController.deleteDiscount(1L); // Call the controller method

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()); // Assert status code
        verify(discountService, times(1)).deleteDiscount(1L); // Verify service interaction
    }

    // Test case for getting all discounts
    @Test
    public void testGetAllDiscounts() {
        List<Discount> discounts = Collections.singletonList(discount); // Create a list of discounts
        when(discountService.findActiveDiscounts()).thenReturn(discounts); // Mock service call

        ResponseEntity<List<Discount>> response = discountController.getAllDiscounts(); // Call the controller method

        assertEquals(HttpStatus.OK, response.getStatusCode()); // Assert status code
        assertEquals(discounts, response.getBody()); // Assert response body
        verify(discountService, times(1)).findActiveDiscounts(); // Verify service interaction
    }

    // Test case for getting a discount that is found
    @Test
    public void testGetDiscountFound() {
        when(discountService.getDiscountDetails(1L)).thenReturn(Optional.of(discount)); // Mock service call

        ResponseEntity<Discount> response = discountController.getDiscount(1L); // Call the controller method

        assertEquals(HttpStatus.OK, response.getStatusCode()); // Assert status code
        assertEquals(discount, response.getBody()); // Assert response body
        verify(discountService, times(1)).getDiscountDetails(1L); // Verify service interaction
    }

    // Test case for getting a discount that is not found
    @Test
    public void testGetDiscountNotFound() {
        when(discountService.getDiscountDetails(1L)).thenReturn(Optional.empty()); // Mock service call

        ResponseEntity<Discount> response = discountController.getDiscount(1L); // Call the controller method

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()); // Assert status code
        assertNull(response.getBody()); // Assert response body is null
        verify(discountService, times(1)).getDiscountDetails(1L); // Verify service interaction
    }
}

