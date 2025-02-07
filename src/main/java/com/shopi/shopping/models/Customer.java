package com.shopi.shopping.models;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "customers")  // Maps the class to the "customers" table in the database

public class Customer  {
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ShoppingCart> shoppingCarts = new ArrayList<>();

    public List<ShoppingCart> getShoppingCarts() {
        return shoppingCarts;
    }

    public void setShoppingCarts(List<ShoppingCart> shoppingCarts) {
        this.shoppingCarts = shoppingCarts;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Last name is required.")
    private String lastName;


    private LocalDate birthDate;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be valid.")
    private String email;


    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be valid.")
    private String phone;


    private String identificationNumber;

    // Default constructor required by JPA
    public Customer() {
    }

    // Private constructor for the Builder pattern
    private Customer(CustomerBuilder builder) {
        this.name = builder.name;
        this.lastName = builder.lastName;
        this.birthDate = builder.birthDate;
        this.email = builder.email;
        this.phone = builder.phone;
        this.identificationNumber = builder.identificationNumber;
    }

    // Getters
    public Long getId() {
        return id;
    }

    // Setter for ID (for updates)
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    // Static Builder class
    public static class CustomerBuilder  {
        private String name;
        private String lastName;
        private LocalDate birthDate;
        private String email;
        private String phone;
        private String identificationNumber;

        // Mandatory constructor for name and last name
        public CustomerBuilder(String name, String lastName) {
            this.name = name;
            this.lastName = lastName;
        }

        public CustomerBuilder setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public CustomerBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public CustomerBuilder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public CustomerBuilder setIdentificationNumber(String identificationNumber) {
            this.identificationNumber = identificationNumber;
            return this;
        }


        public CustomerBuilder() {
        }

        public Customer build() {
            // Validaciones para campos obligatorios
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Name is required.");
            }
            if (lastName == null || lastName.isEmpty()) {
                throw new IllegalArgumentException("Last name is required.");
            }
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("Email is required.");
            }

            return new Customer(this);
        }
    }

    @Override
    public String toString() {
        return String.format("Customer ID: %d, Name: %s %s, Birth Date: %s, Email: %s, Phone: %s, Identification: %s",
                id, name, lastName, birthDate, email, phone, identificationNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return id != null && id.equals(customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
