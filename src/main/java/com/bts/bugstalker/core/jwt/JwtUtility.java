package com.bts.bugstalker.core.jwt;

import lombok.experimental.UtilityClass;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

@UtilityClass
public final class JwtUtility {

    public static final String AUTH_HEADER_NAME = "Authorization";

    public static final String AUTH_TOKEN_PREFIX = "Bearer ";

    public static final String JWT_BLACKLIST = "jwtBlacklist:";

    public static String getAuthHeaderContent(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER_NAME);
    }

    public static String getTokenContent(String token) {
        return token.replace(AUTH_TOKEN_PREFIX, "");
    }

    public boolean hasAuthToken(String token) {
        return StringUtils.isNotBlank(token) && token.startsWith(AUTH_TOKEN_PREFIX);
    }

    public static String stripOfPrefix(String token) {
        return token.replace(AUTH_TOKEN_PREFIX, "");
    }

    public static String createBlacklistCacheKey(String token) {
        return JWT_BLACKLIST.concat(createTokenHash(token));
    }

    public static String createTokenHash(String token) {
        return DigestUtils.sha256Hex(token);
    }
}


