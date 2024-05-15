package com.bts.bugstalker.config;

import com.bts.bugstalker.util.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Objects;

@TestConfiguration
@RequiredArgsConstructor
public class RedisMockConfig {

    @Autowired
    private RedisProperties redisProperties;

    private RedisServer redisServer;

    @PostConstruct
    public void setUp() throws IOException {
        this.redisServer = new RedisServer(redisProperties.getRedisPort());
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() throws IOException {
        if (Objects.nonNull(redisServer) && redisServer.isActive()) {
            redisServer.stop();
        }
    }
}
