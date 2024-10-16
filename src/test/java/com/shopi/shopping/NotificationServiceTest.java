package com.shopi.shopping;
import static org.mockito.Mockito.*;
import com.shopi.shopping.models.Event;
import com.shopi.shopping.configuration.RabbitConfig;
import com.shopi.shopping.services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.AmqpTemplate;
public class NotificationServiceTest {

    @Mock
    private AmqpTemplate amqpTemplate;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testNotify_Success() {
        Event event = new Event("BIRTHDAY", "Happy Birthday!");

        // Call the method under test
        notificationService.notify(event);

        // Verify that the message was sent to RabbitMQ
        verify(amqpTemplate, times(1)).convertAndSend(RabbitConfig.NOTIFICATION_EXCHANGE, event.getEventType(), event);
    }

    @Test
    public void testGetNotifications_BirthdayEvent() {
        Event event = new Event("BIRTHDAY", "Happy Birthday!");

        // Call the method that handles the event
        notificationService.getNotifications(event);

        // Add any additional assertions or verifications for SMS sending
    }

    @Test
    public void testGetNotifications_SubmittedEvent() {
        Event event = new Event("SUBMITTED", "Your submission was successful!");

        // Call the method that handles the event
        notificationService.getNotifications(event);

        // Add any additional assertions or verifications for email sending
    }

    @Test
    public void testGetNotifications_GenericEvent() {
        Event event = new Event("OTHER", "This is a generic notification.");

        // Call the method that handles the event
        notificationService.getNotifications(event);

        // Add any additional assertions or verifications for generic notifications
    }
}

