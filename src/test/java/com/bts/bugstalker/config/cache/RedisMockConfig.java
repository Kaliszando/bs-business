package com.bts.bugstalker.config.cache;

import com.bts.bugstalker.util.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import redis.embedded.RedisServer;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Slf4j
@TestConfiguration
@RequiredArgsConstructor
public class RedisMockConfig {

    @Autowired
    private LettuceConnectionFactory lettuceConnectionFactory;

    private RedisServer redisServer;

    @Bean
    public RedisServer redisServer(RedisProperties redisProperties) throws IOException {
        redisServer = new RedisServer(redisProperties.getRedisPort());

        log.info("Starting embedded redis server on port: {}", redisProperties.getRedisPort());
        redisServer.start();
        return redisServer;
    }

    @Bean
    @Primary
    public LettuceConnectionFactory lettuceConnectionFactoryNoPassword(RedisProperties redisProperties,
                                                                       LettuceClientConfiguration clientConfiguration) {
        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(
                redisProperties.getRedisHost(), redisProperties.getRedisPort());
        return new LettuceConnectionFactory(serverConfig, clientConfiguration);
    }

    @PreDestroy
    public void preDestroy() throws IOException {
        redisServer.stop();
    }

    @SneakyThrows
    @EventListener(ContextClosedEvent.class)
    public void stopRedis() {
        if (lettuceConnectionFactory != null) {
            lettuceConnectionFactory.destroy();
        }

        if (redisServer != null) {
            redisServer.stop();
        }
    }
}
