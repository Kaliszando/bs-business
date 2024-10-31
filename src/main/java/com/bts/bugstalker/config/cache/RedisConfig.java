package com.bts.bugstalker.config.cache;

import com.bts.bugstalker.feature.cache.jwt.JwtCache;
import com.bts.bugstalker.util.properties.RedisProperties;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@EnableAutoConfiguration(exclude = RedisRepositoriesAutoConfiguration.class)
@EnableRedisRepositories(basePackageClasses = JwtCache.class)
@EnableCaching
@Configuration
public class RedisConfig {

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(0);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        return poolConfig;
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

    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
