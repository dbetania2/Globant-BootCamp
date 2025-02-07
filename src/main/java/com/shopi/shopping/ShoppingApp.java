package com.shopi.shopping;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.shopi.shopping.repositories")
@EnableCaching
public class ShoppingApp {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingApp.class, args);
    }
}
