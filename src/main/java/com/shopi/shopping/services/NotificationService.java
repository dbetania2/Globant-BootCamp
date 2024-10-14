package com.shopi.shopping.services;
import com.shopi.shopping.models.Event;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
@Service
public class NotificationService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void notify(Event event) {
        // Logic to send message to RabbitMQ
        amqpTemplate.convertAndSend("notificationExchange", "notify", event);
    }

    @RabbitListener(queues = "notifications")
    public void getNotifications(Event event) {
        System.out.println("Received event: " + event);
        processEvent(event); // Call a method to handle the event
    }

    private void processEvent(Event event) {
        // Logic to generate the message based on the event
        String message;

        // Send SMS if it's the customer's birthday
        if ("BIRTHDAY".equals(event.getEventType())) {
            message = "Happy Birthday! We wish you a wonderful day!";
            send(message, "SMS");
        }
        // Send Email if the cart has passed to SUBMITTED state
        else if ("SUBMITTED".equals(event.getEventType())) {
            message = "The cart with ID " + event.getCartId() + " has been submitted. Thank you for your purchase!";
            send(message, "email");
        }
    }

    public void send(String message, String destinationType) {
        if ("SMS".equals(destinationType)) {
            sendSMS(message);
        } else if ("email".equals(destinationType)) {
            sendEmail(message);
        }
    }

    public void sendSMS(String message) {
        // Logic to send SMS
        System.out.println("Sending SMS: " + message);
    }

    public void sendEmail(String message) {
        // Logic to send email
        System.out.println("Sending Email: " + message);
    }
}
