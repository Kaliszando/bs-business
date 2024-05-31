package com.bts.bugstalker.utils;

import com.bts.bugstalker.core.common.enums.UserRole;
import com.bts.bugstalker.feature.cache.jwt.JwtHelper;
import io.restassured.http.Header;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@Component
@RequiredArgsConstructor
public class AuthorizationHeaderMockTool {

    public final static Pattern JWT_TOKEN = Pattern.compile("^Bearer ([a-zA-Z0-9_=]+).([a-zA-Z0-9_=]+).([a-zA-Z0-9_\\-+/=]*)");

    private final JwtHelper jwtHelper;

    public static boolean isJwtToken(String token) {
        return JWT_TOKEN.matcher(token).matches();
    }

    public Header prepare(String username) {
        String token = jwtHelper.createJwtTokenWithPrefix(username);
        assertThat(isJwtToken(token)).isTrue();
        return new Header(JwtHelper.AUTH_HEADER_NAME, token);
    }

    public Header prepare(UserRole role) {
        if (role == null) {
            return new Header(JwtHelper.AUTH_HEADER_NAME, "");
        }
        return switch (role) {
            case ADMIN -> prepare("JohnDoe334");
            case USER -> prepare("JamesSmith678");
            case GUEST -> prepare("MariaMartinez645");
        };
    }
}
