package com.bts.bugstalker.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bts.bugstalker.core.common.enums.UserRole;
import com.bts.bugstalker.util.properties.JwtProperties;
import io.restassured.http.Header;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@Component
@RequiredArgsConstructor
public class AuthorizationHeaderMockTool {

    private final static String AUTH_HEADER_NAME = "Authorization";

    private final static String AUTH_TOKEN_PREFIX = "Bearer ";

    public final static Pattern JWT_TOKEN = Pattern.compile("^Bearer ([a-zA-Z0-9_=]+).([a-zA-Z0-9_=]+).([a-zA-Z0-9_\\-+/=]*)");

    private final JwtProperties jwtProperties;

    public Header prepare(String username) {
        String token = AUTH_TOKEN_PREFIX.concat(JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(jwtProperties.getExpirationTimeMillis())))
                .sign(Algorithm.HMAC256(jwtProperties.getSecret())));
        assertThat(isJwtToken(token)).isTrue();
        return new Header(AUTH_HEADER_NAME, token);
    }

    public static boolean isJwtToken(String token) {
        return JWT_TOKEN.matcher(token).matches();
    }

    public Header prepare(UserRole role) {
        if (role == null) {
            return new Header(AUTH_HEADER_NAME, "");
        }
        return switch (role) {
            case ADMIN -> prepare("JohnDoe334");
            case USER -> prepare("JamesSmith678");
            case GUEST -> prepare("MariaMartinez645");
        };
    }
}
