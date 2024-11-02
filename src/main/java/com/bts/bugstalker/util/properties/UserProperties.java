package com.bts.bugstalker.util.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter @RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "user")
public final class UserProperties {

    public final static String USER_BY_USERNAME = "userByUsername";

    @Value("${user.cache.ttl.min:16}")
    public final Long userCacheTtl;
}
