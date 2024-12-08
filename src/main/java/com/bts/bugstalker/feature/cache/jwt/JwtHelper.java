package com.bts.bugstalker.feature.cache.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bts.bugstalker.feature.cache.CacheRepository;
import com.bts.bugstalker.util.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JwtHelper {

    public static final String AUTH_HEADER_NAME = "Authorization";

    public static final String AUTH_TOKEN_PREFIX = "Bearer ";

    public static final String JWT_BLACKLIST = "jwtBlacklist:";

    private final JwtProperties jwtProperties;

    private final CacheRepository cacheRepository;

    public String createJwtToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(currentTimePlus(jwtProperties.getExpirationTime()))
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
    }

    public Date currentTimePlus(Long minutes) {
        return new Date(System.currentTimeMillis() + minutes * 60 * 1000);
    }

    public String createJwtTokenWithPrefix(String username) {
        return AUTH_TOKEN_PREFIX.concat(createJwtToken(username));
    }

    public void addToBlacklist(String token) {
        String key = JWT_BLACKLIST.concat(hashToken(token));
        cacheRepository.setValue(key, "1", jwtProperties.getJwtCacheTtl() * 60);
    }

    public boolean isBlacklisted(String token) {
        return cacheRepository.exists(JWT_BLACKLIST.concat(hashToken(token)));
    }

    public static String hashToken(String token) {
        return DigestUtils.sha256Hex(token);
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

    public static String stripOfPrefix(String token) {
        return token.replace(AUTH_TOKEN_PREFIX, "");
    }

    private static String getTokenContent(HttpServletRequest request) {
        return getAuthHeader(request).replace(AUTH_TOKEN_PREFIX, "");
    }

    private static String getAuthHeader(HttpServletRequest request) {
        return request.getHeader(JwtHelper.AUTH_HEADER_NAME);
    }
}
