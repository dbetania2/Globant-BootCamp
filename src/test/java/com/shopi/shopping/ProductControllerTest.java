package com.shopi.shopping;

import com.shopi.shopping.models.products.Book;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.services.ProductService;
import com.shopi.shopping.factories.ProductFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.Arrays;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductFactory productFactory;

    private Product book1;
    private Product book2;

    @BeforeEach
    public void setUp() {
        // Setup products of type Book
        book1 = new Book(new BigDecimal("9.99"), "Book1", "Default description");
        book2 = new Book(new BigDecimal("19.99"), "Book2", "Default description");
    }

    @Test
    public void testGetAllProducts() throws Exception {
        // Mocking the service to return a list of products
        Mockito.when(productService.getAllProducts()).thenReturn(Arrays.asList(book1, book2));

        // Perform GET request
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk()) // Expecting 200 OK
                .andExpect(jsonPath("$[0].name").value("Book1"))
                .andExpect(jsonPath("$[1].name").value("Book2"));
    }

    @Test
    public void testGetProductById_Found() throws Exception {
        // Mocking the service to return a product by ID
        Mockito.when(productService.getProductById(anyLong())).thenReturn(book1);

        // Perform GET request for a valid ID
        mockMvc.perform(get("/products/{id}", 1L))
                .andExpect(status().isOk()) // Expecting 200 OK
                .andExpect(jsonPath("$.name").value("Book1"));
    }

    @Test
    public void testGetProductById_NotFound() throws Exception {
        // Mocking the service to return null for a non-existing product
        Mockito.when(productService.getProductById(anyLong())).thenReturn(null);

        // Perform GET request for an invalid ID
        mockMvc.perform(get("/products/{id}", 999L))
                .andExpect(status().isNotFound()); // Expecting 404 Not Found
    }

    @Test
    public void testCreateProduct() throws Exception {
        // Mocking the factory to create a product of type Book
        Mockito.when(productFactory.createProduct(anyString(), anyString(), any(BigDecimal.class)))
                .thenReturn(book1);

        // Mocking the service to save the product
        Mockito.when(productService.createProduct(anyString(), anyString(), any(BigDecimal.class)))
                .thenReturn(book1);

        // Perform POST request to create a product
        mockMvc.perform(post("/products")
                        .param("category", "BOOK")
                        .param("name", "Book1")
                        .param("price", "9.99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()) // Expecting 201 Created
                .andExpect(jsonPath("$.name").value("Book1"));
    }

    @Test
    public void testUpdateProduct_Success() throws Exception {
        // Mocking the service to return an existing product
        Mockito.when(productService.getProductById(anyLong())).thenReturn(book1);

        // Mocking the service to update the product
        Mockito.doNothing().when(productService).updateProduct(any(Product.class));

        // Perform PUT request to update the product
        mockMvc.perform(put("/products/{id}", 1L)
                        .param("price", "15.99")
                        .param("name", "UpdatedBook")
                        .param("description", "Updated description")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expecting 200 OK
                .andExpect(jsonPath("$.name").value("UpdatedBook"));
    }

    @Test
    public void testUpdateProduct_NotFound() throws Exception {
        // Mocking the service to return null for a non-existing product
        Mockito.when(productService.getProductById(anyLong())).thenReturn(null);

        // Perform PUT request with an invalid ID
        mockMvc.perform(put("/products/{id}", 999L)
                        .param("price", "15.99")
                        .param("name", "UpdatedBook")
                        .param("description", "Updated description")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Expecting 404 Not Found
    }

    @Test
    public void testDeleteProduct_Success() throws Exception {
        // Mocking the service to successfully delete a product
        Mockito.doNothing().when(productService).deleteProductWithCache(anyLong());

        // Perform DELETE request with a valid ID
        mockMvc.perform(delete("/products/{id}", 1L))
                .andExpect(status().isNoContent()); // Expecting 204 No Content
    }

    @Test
    public void testDeleteProduct_NotFound() throws Exception {
        // Mocking the service to throw an exception for a non-existing product
        Mockito.doThrow(new IllegalArgumentException("Product not found")).when(productService).deleteProductWithCache(anyLong());

        // Perform DELETE request with an invalid ID
        mockMvc.perform(delete("/products/{id}", 999L))
                .andExpect(status().isNotFound()); // Expecting 404 Not Found
    }
}
