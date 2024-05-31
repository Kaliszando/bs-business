package com.bts.bugstalker.config.cache;

import com.bts.bugstalker.util.properties.RedisProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisPropertiesConfig {

    @Bean
    public RedisProperties redisProperties(
            @Value("${spring.redis.port}") int redisPort,
            @Value("${spring.redis.host}") String redisHost,
            @Value("${spring.redis.password}") String password) {
        return new RedisProperties(redisPort, redisHost, password);
    }
}
