package com.bts.bugstalker.util.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter @RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "jwt")
public final class JwtProperties {

    private final String expirationTimeMillis;

    private final String secret;

}
