package com.bts.bugstalker.feature.cache.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bts.bugstalker.util.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JwtHelper {

    public static final String AUTH_HEADER_NAME = "Authorization";

    public static final String AUTH_TOKEN_PREFIX = "Bearer ";

    private final JwtProperties jwtProperties;

    private final JwtCache jwtCache;

    public void addToBlacklist(String token) {
        JwtEntity jwtEntity = createEntity(token);
        jwtCache.save(jwtEntity);
    }

    public boolean hasAuthToken(HttpServletRequest request) {
        String authHeader = getAuthHeader(request);
        return authHeader != null && authHeader.startsWith(AUTH_TOKEN_PREFIX);
    }

    public Optional<String> extractToken(HttpServletRequest request) {
        if (!hasAuthToken(request)) {
            return Optional.empty();
        }

        String token = getAuthHeader(request).replace(AUTH_TOKEN_PREFIX, "");
        return Optional.of(token);
    }

    public boolean isBlacklisted(String token) {
        return jwtCache.existsByToken(token);
    }

    public String createToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpirationTimeMillis()))
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
    }

    private JwtEntity createEntity(String token) {
        return new JwtEntity(jwtProperties.getExpirationTimeMillis(), token);
    }

    private static String getAuthHeader(HttpServletRequest request) {
        return request.getHeader(JwtHelper.AUTH_HEADER_NAME);
    }
}
