package com.bts.bugstalker.core.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bts.bugstalker.core.cache.CacheRepository;
import com.bts.bugstalker.util.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.bts.bugstalker.core.jwt.JwtUtility.createBlacklistCacheKey;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    private final CacheRepository cacheRepository;

    public void addToBlacklist(String token) {
        String key = createBlacklistCacheKey(token);
        cacheRepository.setValue(key, "1", jwtProperties.getJwtCacheTtl() * 60);
    }

    public boolean isBlacklisted(String token) {
        return cacheRepository.exists(createBlacklistCacheKey(token));
    }

    public Optional<String> extractToken(String possibleToken) {
        if (JwtUtility.hasAuthToken(possibleToken)) {
            return Optional.of(JwtUtility.getTokenContent(possibleToken));
        }

        return Optional.empty();
    }

    public String extractUsernameFromToken(String token) {
        return JWT.require(Algorithm.HMAC256(jwtProperties.getSecret()))
                .build()
                .verify(token)
                .getSubject();
    }
}
