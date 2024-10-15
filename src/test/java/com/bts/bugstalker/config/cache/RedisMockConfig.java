package com.bts.bugstalker.config.cache;

import com.bts.bugstalker.util.properties.RedisProperties;
import com.redis.testcontainers.RedisContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@Testcontainers
@TestConfiguration
public class RedisMockConfig {

    @Container
    public static RedisContainer redisContainer = new RedisContainer("redis:6.2.1");

    @Bean
    @Primary
    public RedisProperties redisPropertiesMock() {
        redisContainer.start();
        log.info("Redis container started on port: {}", redisContainer.getFirstMappedPort());
        return new RedisProperties(redisContainer.getRedisPort(), redisContainer.getHost(), "password");
    }
}