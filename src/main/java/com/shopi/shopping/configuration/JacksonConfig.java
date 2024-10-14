package com.shopi.shopping.configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Indicates that this class contains Spring configuration
public class JacksonConfig {

    // Defines a bean for ObjectMapper which will be used for JSON serialization/deserialization
    @Bean
    public ObjectMapper objectMapper() {
        // Create a new instance of ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Register the JavaTimeModule to handle Java 8 date and time types
        objectMapper.registerModule(new JavaTimeModule());

        // Return the configured ObjectMapper
        return objectMapper;
    }
}
