package com.shopi.shopping.models;

import java.io.Serializable;

public class Notification implements Serializable {
    private String message;
    private String destination; // Can be "SMS" or "email"
    private String recipient;    // Email address or phone number

    // Default constructor required for deserialization
    public Notification() {}

    public Notification(String message, String destination, String recipient) {
        this.message = message;
        this.destination = destination;
        this.recipient = recipient;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
