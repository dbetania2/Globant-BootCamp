package com.shopi.shopping;
import com.shopi.shopping.models.Event;
import com.shopi.shopping.services.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    void testNotify() {
        Event event = new Event(1L, "BIRTHDAY");
        notificationService.notify(event);

        // This should be verified in the RabbitMQ panel
    }

    @Test
    void testGetNotifications_BirthdayEvent() {
        Event birthdayEvent = new Event(1L, "BIRTHDAY");
        notificationService.getNotifications(birthdayEvent);

        // This should be verified in the RabbitMQ panel
    }

    @Test
    void testGetNotifications_SubmittedEvent() {
        Event submittedEvent = new Event(1L, "SUBMITTED");
        notificationService.getNotifications(submittedEvent);

        // This should be verified in the RabbitMQ panel
        String expectedMessage = "The cart with ID " + submittedEvent.getCartId() + " has been submitted. Thank you for your purchase!";
    }
}
