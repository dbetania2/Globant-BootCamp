package com.shopi.shopping.configuration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching

public class CacheConfig {

    // This method defines a bean for CaffeineCacheManager, which is responsible for managing the caches
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("products","shoppingCarts","shoppingCartsByStatus");
    }
}






