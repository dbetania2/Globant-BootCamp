package main.java.com.shopi.shopping.models;

import java.time.LocalDate;

public class Customer {
    private static long customerIdCounter = 100;  // Starts at 100 as an example
    private long id;
    private String name;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String identificationNumber;

    // Constructor privado
    private Customer(CustomerBuilder builder) {
        this.id = generateCustomerId();  // Generates a unique ID for the customer
        this.name = builder.name;
        this.lastName = builder.lastName;
        this.birthDate = builder.birthDate;
        this.email = builder.email;
        this.phone = builder.phone;
        this.identificationNumber = builder.identificationNumber;
    }

    // Method to generate a unique ID
    private synchronized long generateCustomerId() {
        return customerIdCounter++;  // Returns the current value and then increments
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;  // Getter for name
    }

    public String getLastName() {
        return lastName;  // Getter for last name
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

    // Builder class
    public static class CustomerBuilder {
        private String name;
        private String lastName;
        private LocalDate birthDate;
        private String email;
        private String phone;
        private String identificationNumber;

        // Constructor obligatorio para el nombre y apellido
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

        public Customer build() {
            return new Customer(this);
        }
    }

    @Override
    public String toString() {
        return String.format("Name: %s %s, Identification: %s",
                 name, lastName, identificationNumber);
    }
}

