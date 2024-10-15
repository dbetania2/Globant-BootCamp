package com.shopi.shopping.configuration;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitConfig {

    public static final String NOTIFICATION_QUEUE = "notifications"; // Name of the queue
    public static final String NOTIFICATION_EXCHANGE = "notificationExchange"; // Name of the exchange
    public static final String ROUTING_KEY = "cart.status.*"; // Routing key pattern for binding

    // Bean definition for the notification queue
    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true); // Create a durable queue
    }

    // Bean definition for the notification exchange
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE); // Create a topic exchange
    }

    // Bean definition for binding the queue to the exchange with the specified routing key
    @Bean
    public Binding notificationBinding(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue) // Bind the notification queue
                .to(notificationExchange) // to the notification exchange
                .with(ROUTING_KEY); // using the specified routing key
    }
}
