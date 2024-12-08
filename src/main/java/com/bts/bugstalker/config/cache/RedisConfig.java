package com.bts.bugstalker.config.cache;

import com.bts.bugstalker.util.properties.RedisProperties;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@EnableAutoConfiguration(exclude = RedisRepositoriesAutoConfiguration.class)
@EnableCaching
@Configuration
public class RedisConfig {

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(32);
        poolConfig.setMinIdle(4);

        poolConfig.setTestWhileIdle(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestOnBorrow(true);

        poolConfig.setTimeBetweenEvictionRuns(Duration.ofSeconds(30));
        poolConfig.setMinEvictableIdleTime(Duration.ofSeconds(60));
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }

    @Bean
    public JedisPool jedisPool(JedisPoolConfig poolConfig, RedisProperties redisProperties) {
        return new JedisPool(poolConfig, redisProperties.getRedisHost(), redisProperties.getRedisPort());
    }

    @Bean
    public JedisClientConfiguration jedisClientConfiguration(JedisPoolConfig poolConfig) {
        return JedisClientConfiguration.builder()
                .usePooling()
                .poolConfig(poolConfig)
                .build();
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(RedisProperties redisProperties, JedisClientConfiguration clientConfiguration) {
        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(
                redisProperties.getRedisHost(), redisProperties.getRedisPort());
        return new JedisConnectionFactory(serverConfig, clientConfiguration);
    }
}
