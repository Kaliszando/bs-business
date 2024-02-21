package com.bts.bugstalker.core.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserLoginDoesNotMatchAnyResultException extends RuntimeException {

    private static final String INVALID_LOGIN_MSG = "No user with login %s found";

    public UserLoginDoesNotMatchAnyResultException(String login) {
        super(String.format(INVALID_LOGIN_MSG, login));
    }
}
