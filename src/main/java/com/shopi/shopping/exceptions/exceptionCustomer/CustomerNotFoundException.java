package com.shopi.shopping.exceptions.exceptionCustomer;

/**
 * Exception thrown when a customer is not found.
 */
public class CustomerNotFoundException extends RuntimeException {

    /**
     * Constructs a new CustomerNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public CustomerNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new CustomerNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}