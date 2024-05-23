package com.bts.bugstalker.feature.cache.jwt;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Data
@RedisHash("JwtEntity")
public class JwtEntity implements Serializable {

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiration;

    @Id
    private Long id;

    @Indexed
    private String token;

    public JwtEntity(Long expiration, String token) {
        this.expiration = expiration;
        this.token = token;
    }
}
