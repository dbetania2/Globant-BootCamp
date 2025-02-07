package com.shopi.shopping;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shopi.shopping.controllers.CustomerController;
import com.shopi.shopping.models.Customer;
import com.shopi.shopping.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.time.LocalDate;

public class CustomerControllerTest {

    @Mock // Create a mock instance of CustomerService
    private CustomerService customerService;

    @InjectMocks // Create an instance of CustomerController and inject the mocked CustomerService into it
    private CustomerController customerController;

    private MockMvc mockMvc; // MockMvc instance for testing the controller

    private ObjectMapper objectMapper; // ObjectMapper instance for JSON conversion

    @BeforeEach // This method runs before each test
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the mocks
        mockMvc = standaloneSetup(customerController).build(); // Set up MockMvc for the CustomerController

        // Configure ObjectMapper to handle LocalDate serialization
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register the module for LocalDate support
    }

    @Test // This annotation marks the method as a test case
    public void testCreateCustomer() throws Exception {
        // Create a new Customer object using the Builder pattern
        Customer customer = new Customer.CustomerBuilder("John", "Doe")
                .setBirthDate(LocalDate.of(1990, 1, 1)) // Set the birth date
                .setEmail("john.doe@example.com") // Set the email
                .setPhone("+1234567890") // Set the phone number
                .setIdentificationNumber("123456789") // Set the identification number
                .build(); // Build the Customer object

        // Mock the behavior of the customerService to return the created customer
        when(customerService.createCustomer(any(Customer.class))).thenReturn(customer);

        // Perform a POST request to the controller with the customer object as the body
        mockMvc.perform(post("/api/customers") // Send a POST request to the /api/customers endpoint
                        .contentType(MediaType.APPLICATION_JSON) // Set the content type to JSON
                        .content(objectMapper.writeValueAsString(customer))) // Convert the Customer object to JSON
                .andExpect(MockMvcResultMatchers.status().isCreated()) // Expect a 201 Created status
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John")) // Expect the name in the JSON response
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe")); // Expect the last name in the JSON response
    }
}
