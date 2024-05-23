package com.bts.bugstalker.config.cache;

import com.bts.bugstalker.feature.cache.jwt.JwtCache;
import com.bts.bugstalker.util.properties.RedisProperties;
import io.lettuce.core.ClientOptions;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableAutoConfiguration(exclude = RedisRepositoriesAutoConfiguration.class)
@EnableRedisRepositories(basePackageClasses = JwtCache.class)
@EnableCaching
@Configuration
public class RedisConfig {

    @Bean
    public LettuceClientConfiguration lettuceClientConfiguration() {
        return LettuceClientConfiguration.builder()
                .clientOptions(ClientOptions.builder()
                        .autoReconnect(false)
                        .build())
                .build();
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(RedisProperties redisProperties, LettuceClientConfiguration clientConfiguration) {
        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(
                redisProperties.getRedisHost(), redisProperties.getRedisPort());
        return new LettuceConnectionFactory(serverConfig, clientConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
