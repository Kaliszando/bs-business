package com.bts.bugstalker.core.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bts.bugstalker.util.parameters.DateProvider;
import com.bts.bugstalker.util.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtFactory {

    private final JwtProperties jwtProperties;

    public String createJwtToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(DateProvider.currentTimePlusMinutes(jwtProperties.getExpirationTime()))
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
    }

    public String createJwtTokenWithPrefix(String username) {
        return JwtUtility.AUTH_TOKEN_PREFIX.concat(createJwtToken(username));
    }
}
