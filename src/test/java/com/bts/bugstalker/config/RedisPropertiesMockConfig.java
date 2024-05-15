package com.bts.bugstalker.config;

import com.bts.bugstalker.util.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.net.ServerSocket;

import static org.assertj.core.api.Assertions.assertThat;

@TestConfiguration
@RequiredArgsConstructor
public class RedisPropertiesMockConfig {

    @Bean
    @Primary
    public RedisProperties redisPropertiesMock() {
        return new RedisProperties(findFreePort(), "localhost");
    }

    private int findFreePort() {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            assertThat(serverSocket).isNotNull();
            assertThat(serverSocket.getLocalPort()).isGreaterThan(0);
            return serverSocket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException("Could not find any free port for embedded Redis");
        }
    }
}