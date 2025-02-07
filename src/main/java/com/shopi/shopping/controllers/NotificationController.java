package com.shopi.shopping.controllers;
import com.shopi.shopping.models.Event;
import com.shopi.shopping.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Operation(
            summary = "Send a notification to RabbitMQ",
            description = "Sends an event as a notification to RabbitMQ."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification sent successfully."),
            @ApiResponse(responseCode = "400", description = "Event type and message cannot be null.")
    })
    @PostMapping("/sendNotification")
    public ResponseEntity<String> sendNotification(@RequestBody Event event) {
        // Validate the incoming event
        if (event.getEventType() == null || event.getMessage() == null) {
            return ResponseEntity.badRequest().body("Event type and message cannot be null.");
        }

        notificationService.notify(event);  // Sends the event to RabbitMQ
        String responseMessage = String.format("Notification sent successfully! Event Type: %s, Message: %s",
                event.getEventType(), event.getMessage());

        return ResponseEntity.ok(responseMessage);
    }

    @Operation(
            summary = "Get notification status from RabbitMQ",
            description = "Returns the current status of notification listening from RabbitMQ."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Currently listening for notifications.")
    })
    @GetMapping("/getNotificationStatus")
    public ResponseEntity<String> getNotificationStatus() {
        // This method could return a message indicating that notifications are being listened for
        return ResponseEntity.ok("Listening for notifications from RabbitMQ.");
    }

}
