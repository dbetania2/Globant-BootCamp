package com.shopi.shopping.models;

import java.io.Serializable;

public class Event implements Serializable {
    private Long id; // Cart ID
    private String eventType; // Event type (e.g., "SUBMITTED", "CANCELLED")
    private long timestamp; // Event timestamp

    public Event(Long cartId, String eventType) {
        this.id = cartId;
        this.eventType = eventType;
        this.timestamp = System.currentTimeMillis(); // Current timestamp
    }

    public Long getCartId() {
        return id;
    }

    public String getEventType() {
        return eventType;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
