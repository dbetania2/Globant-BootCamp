<<<<<<<< HEAD:src/main/java/com/shopi/shopping/models/customer/Customer.java
package models.customer;
========
package com.shopi.shopping.models;
>>>>>>>> topic2formerge:src/main/java/com/shopi/shopping/models/Customer.java

import java.time.LocalDate;
public class Customer {
    private static long customerIdCounter = 1; // Example static counter for unique customer IDs
    private long id;
    private String name;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String identificationNumber;

    // Private constructor to enforce object creation through the Builder pattern
    private Customer(CustomerBuilder builder) {
        this.id = customerIdCounter++;          // Assign a unique ID
        this.name = builder.name;
        this.lastName = builder.lastName;
        this.birthDate = builder.birthDate;
        this.email = builder.email;
        this.phone = builder.phone;
        this.identificationNumber = builder.identificationNumber;
    }

    // Getters
    public long getId() {
        return id;
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

    // Static Builder class
    public static class CustomerBuilder {
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

        public Customer build() {
            // Validations
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Name is required.");
            }
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("Email is required.");
            }
            return new Customer(this); // Create a new instance of Customer
        }
    }

    @Override
    public String toString() {
        return String.format("Customer ID: %d, Name: %s %s, Birth Date: %s, Email: %s, Phone: %s, Identification: %s",
                id, name, lastName, birthDate, email, phone, identificationNumber);
    }
}
<<<<<<<< HEAD:src/main/java/com/shopi/shopping/models/customer/Customer.java


========
>>>>>>>> topic2formerge:src/main/java/com/shopi/shopping/models/Customer.java
