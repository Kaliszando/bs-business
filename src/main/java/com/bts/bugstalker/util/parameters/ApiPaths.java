package com.bts.bugstalker.util.parameters;

public final class ApiPaths {

    public static final String V1 = "/api/v1";

    public static final String ADMIN = "/api/v1/admin";

    public static final String SIGN_IN = "/api/v1/auth/sign-in";

    public static final String SIGN_UP = "/api/v1/auth/sign-up";

    public static final String PING = "/api/v1/auth/ping";

    public static final String[] PUBLIC = {
            SIGN_IN, SIGN_UP,
            "/swagger-ui/**", "/v3/api-docs/**", "/h2/**", "/error", "/", "/favicon.ico",
            "/api/v1/**"
    };

}
