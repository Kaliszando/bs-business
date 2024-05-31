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

    public String createJwtToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpirationTimeMillis()))
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
    }

    public String createJwtTokenWithPrefix(String username) {
        return AUTH_TOKEN_PREFIX.concat(createJwtToken(username));
    }

    public void addToBlacklist(String token) {
        JwtEntity jwtEntity = createEntity(token);
        jwtCache.save(jwtEntity);
    }

    public boolean isBlacklisted(String token) {
        return jwtCache.existsByToken(token);
    }

    public boolean hasAuthToken(HttpServletRequest request) {
        String authHeader = getAuthHeader(request);
        return authHeader != null && authHeader.startsWith(AUTH_TOKEN_PREFIX);
    }

    public Optional<String> extractToken(HttpServletRequest request) {
        if (!hasAuthToken(request)) {
            return Optional.empty();
        }

        String token = getTokenContent(request);
        return Optional.of(token);
    }

    public String getTokenIfValid(HttpServletRequest request) {
        if (!hasAuthToken(request)) {
            return null;
        }
        String token = getTokenContent(request);
        if (isBlacklisted(token)) {
            return null;
        }
        return token;
    }

    public String extractUsernameFromToken(String token) {
        return JWT.require(Algorithm.HMAC256(jwtProperties.getSecret()))
                .build()
                .verify(token)
                .getSubject();
    }

    private static String getTokenContent(HttpServletRequest request) {
        return getAuthHeader(request).replace(AUTH_TOKEN_PREFIX, "");
    }

    private JwtEntity createEntity(String token) {
        return new JwtEntity(jwtProperties.getBlacklistedTimeMillis(), token);
    }

    private static String getAuthHeader(HttpServletRequest request) {
        return request.getHeader(JwtHelper.AUTH_HEADER_NAME);
    }
}
