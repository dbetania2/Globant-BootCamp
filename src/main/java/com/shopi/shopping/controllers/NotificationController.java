package com.shopi.shopping.controllers;
import com.shopi.shopping.models.Event;
import com.shopi.shopping.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Method to send a notification to RabbitMQ
    @PostMapping("/sendNotification")
    public ResponseEntity<String> sendNotification(@RequestBody Event event) {
        notificationService.notify(event);  // Sends the event to RabbitMQ
        return ResponseEntity.ok("Notification sent successfully!");
    }

    // Method to get the status of listening for notifications from RabbitMQ
    @GetMapping("/getNotificationStatus")
    public ResponseEntity<String> getNotificationStatus() {
        // This method could return a message indicating that notifications are being listened for
        return ResponseEntity.ok("Listening for notifications from RabbitMQ.");
    }
}
