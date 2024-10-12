package com.shopi.shopping;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.shopi.shopping.models.Customer;
import com.shopi.shopping.repositories.CustomerRepository;
import com.shopi.shopping.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository; // Mocking the CustomerRepository for testing

    @InjectMocks
    private CustomerService customerService; // Injecting the mock repository into the CustomerService

    private Customer.CustomerBuilder customerBuilder; // Builder for creating Customer objects

    @BeforeEach
    void setUp() {
        // Initializing the CustomerBuilder with default values
        customerBuilder = new Customer.CustomerBuilder("John", "Doe")
                .setEmail("john.doe@example.com")
                .setBirthDate(LocalDate.of(1990, 1, 1));
    }

    @Test
    void createCustomer_ShouldSaveCustomer() {
        // Arrange
        Customer customer = new Customer.CustomerBuilder("John", "Doe")
                .setBirthDate(LocalDate.of(1990, 1, 1))
                .setEmail("john.doe@example.com")
                .setPhone("+1234567890")
                .setIdentificationNumber("123456789")
                .build(); // Creando un objeto Customer usando el builder

        // Simular que el repositorio guarda correctamente el cliente
        when(customerRepository.save(any(Customer.class))).thenReturn(customer); // Stubbing el método save

        // Act
        Customer createdCustomer = customerService.createCustomer(customer); // Llamando al método createCustomer con el Customer

        // Assert
        assertNotNull(createdCustomer); // Asegurando que el cliente creado no es nulo
        assertEquals("John", createdCustomer.getName()); // Asegurando que el nombre del cliente creado es correcto
        assertEquals("Doe", createdCustomer.getLastName()); // Asegurando que el apellido del cliente creado es correcto

        // Verificando que el cliente fue guardado correctamente
        verify(customerRepository, times(1)).save(argThat(c ->
                c.getName().equals("John") &&
                        c.getLastName().equals("Doe") &&
                        c.getEmail().equals("john.doe@example.com")
        ));
    }

    @Test
    void getCustomerById_ShouldReturnCustomer() {
        // Arrange
        Customer customer = customerBuilder.build(); // Creating a Customer object
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer)); // Stubbing the findById method

        // Act
        Optional<Customer> foundCustomer = customerService.getCustomerById(1L); // Calling the method to get customer by ID

        // Assert
        assertTrue(foundCustomer.isPresent()); // Asserting that the customer is present
        assertEquals("John", foundCustomer.get().getName()); // Asserting the name of the found customer
    }

    @Test
    void updateCustomer_ShouldUpdateCustomer() {
        // Arrange
        Customer existingCustomer = new Customer.CustomerBuilder("John", "Doe")
                .setBirthDate(LocalDate.of(1990, 1, 1))
                .setEmail("john.doe@example.com")
                .setPhone("1234567890")
                .setIdentificationNumber("ID123456")
                .build();

        // Simula el comportamiento del repositorio para encontrar el cliente existente
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));

        // Simula el comportamiento del repositorio para guardar el cliente actualizado
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Crea el CustomerBuilder para los nuevos datos
        Customer.CustomerBuilder updatedBuilder = new Customer.CustomerBuilder("Jane", "Doe")
                .setBirthDate(LocalDate.of(1992, 2, 2))
                .setEmail("jane.doe@example.com")
                .setPhone("0987654321")
                .setIdentificationNumber("ID654321");

        // Act
        Customer updatedCustomer = customerService.updateCustomer(1L, updatedBuilder.build());

        // Assert
        assertNotNull(updatedCustomer);
        assertEquals("Jane", updatedCustomer.getName());
        assertEquals("Doe", updatedCustomer.getLastName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void deleteCustomer_ShouldDeleteCustomer() {
        // Act
        customerService.deleteCustomer(1L); // Calling the method to delete customer by ID

        // Assert
        verify(customerRepository, times(1)).deleteById(1L); // Verifying that the deleteById method was called once
    }

    @Test
    void getCustomerByEmail_ShouldReturnCustomer() {
        // Arrange
        Customer customer = customerBuilder.build(); // Creating a Customer object
        when(customerRepository.findByEmail(any(String.class))).thenReturn(Optional.of(customer)); // Stubbing the findByEmail method

        // Act
        Optional<Customer> foundCustomer = customerService.getCustomerByEmail("john.doe@example.com"); // Calling the method to get customer by email

        // Assert
        assertTrue(foundCustomer.isPresent()); // Asserting that the customer is present
        assertEquals("John", foundCustomer.get().getName()); // Asserting the name of the found customer
    }

    @Test
    void getCustomersByLastName_ShouldReturnCustomers() {
        // Arrange
        Customer customer = customerBuilder.build(); // Creating a Customer object
        when(customerRepository.findByLastName(any(String.class))).thenReturn(Collections.singletonList(customer)); // Stubbing the findByLastName method

        // Act
        List<Customer> customers = customerService.getCustomersByLastName("Doe"); // Calling the method to get customers by last name

        // Assert
        assertFalse(customers.isEmpty()); // Asserting that the customers list is not empty
        assertEquals("John", customers.get(0).getName()); // Asserting the name of the first customer in the list
    }

    @Test
    void getCustomersByFirstNameContaining_ShouldReturnCustomers() {
        // Arrange
        Customer customer = customerBuilder.build(); // Creating a Customer object
        when(customerRepository.findByNameContaining(any(String.class))).thenReturn(Collections.singletonList(customer)); // Stubbing the findByFirstNameContaining method

        // Act
        List<Customer> customers = customerService.getCustomersByFirstNameContaining("Joh"); // Calling the method to get customers by first name containing substring

        // Assert
        assertFalse(customers.isEmpty()); // Asserting that the customers list is not empty
        assertEquals("John", customers.get(0).getName()); // Asserting the name of the first customer in the list
    }

    @Test
    void getAllCustomers_ShouldReturnCustomers() {
        // Arrange
        Customer customer = customerBuilder.build(); // Creating a Customer object
        when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer)); // Stubbing the findAll method

        // Act
        List<Customer> customers = customerService.getAllCustomers(); // Calling the method to get all customers

        // Assert
        assertFalse(customers.isEmpty()); // Asserting that the customers list is not empty
        assertEquals("John", customers.get(0).getName()); // Asserting the name of the first customer in the list
    }
}
