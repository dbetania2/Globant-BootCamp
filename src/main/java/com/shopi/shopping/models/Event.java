package com.shopi.shopping.models;

import java.io.Serializable;

public class Event  implements Serializable{
    private String eventType; // Tipo de evento
    private String message;   // Mensaje adicional

    // Constructor
    public Event(String eventType, String message) {
        this.eventType = eventType; // Tipo de evento
        this.message = message; // Mensaje adicional
    }

    // Getters y setters
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventType='" + eventType + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
