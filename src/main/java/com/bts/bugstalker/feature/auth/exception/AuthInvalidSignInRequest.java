package com.bts.bugstalker.feature.auth.exception;

import com.bts.bugstalker.common.exception.base.BusinessException;
import com.bts.bugstalker.common.exception.base.ExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class AuthInvalidSignInRequest extends BusinessException {

    private static final String AUTH_INVALID_SIGN_IN_MSG = "Sign in request cannot have null or empty values %S";

    public AuthInvalidSignInRequest(String request) {
        super(ExceptionCode.of("auth.invalid-sign-in-request"),
                String.format(AUTH_INVALID_SIGN_IN_MSG, request));
    }
}
