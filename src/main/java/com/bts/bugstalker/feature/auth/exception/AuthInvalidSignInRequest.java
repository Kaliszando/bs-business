package com.bts.bugstalker.feature.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthInvalidSignInRequest extends RuntimeException {

    private static final String AUTH_INVALID_SIGN_IN_MSG = "Sign in request cannot have null or empty values %S";

    public AuthInvalidSignInRequest(String request) {
        super(String.format(AUTH_INVALID_SIGN_IN_MSG, request));
    }
}
