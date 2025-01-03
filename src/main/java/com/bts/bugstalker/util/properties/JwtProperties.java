package com.bts.bugstalker.util.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter @RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "jwt")
public final class JwtProperties {

    private final String secret;

    @Value("${jwt.expiration.min:15}")
    private final Long expirationTime;

    @Value("${jwt.cache.ttl.min:16}")
    private final Long jwtCacheTtl;
}
