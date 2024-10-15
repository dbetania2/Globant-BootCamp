package com.shopi.shopping.services;
import com.shopi.shopping.configuration.RabbitConfig;
import com.shopi.shopping.models.Event;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Service
public class NotificationService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    // Method that sends notifications to RabbitMQ
    public void notify(Event event) {
        try {
            String exchangeName = RabbitConfig.NOTIFICATION_EXCHANGE; // Use the configured exchange
            String routingKey = event.getEventType(); // Use the event type as the routing key

            // Send the event to RabbitMQ
            amqpTemplate.convertAndSend(exchangeName, routingKey, event);
            System.out.println("Event sent to RabbitMQ: " + event); // Simulating the send
        } catch (Exception e) {
            System.err.println("Failed to send event to RabbitMQ: " + e.getMessage());
        }
    }

    // Method that receives notifications from RabbitMQ and directly handles the event
    @RabbitListener(queues = "notifications")
    public void getNotifications(Event event) {
        System.out.println("Received event: " + event);

        // Logic to decide the message and how to send it
        String message;

        if ("BIRTHDAY".equals(event.getEventType())) {
            message = "Happy Birthday! We wish you a wonderful day!";
            System.out.println("Sending SMS: " + message);  // Simulating sending an SMS
        } else if ("SUBMITTED".equals(event.getEventType())) {
            message = "The cart with ID " + event.getCartId() + " has been submitted. Thank you for your purchase!";
            System.out.println("Sending Email: " + message);  // Simulating sending an email
        } else {
            message = "Notification for event: " + event.getEventType();
            System.out.println("Handling generic notification: " + message);
        }
    }
}