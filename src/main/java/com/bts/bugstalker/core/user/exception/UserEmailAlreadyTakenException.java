package com.bts.bugstalker.core.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserEmailAlreadyTakenException extends RuntimeException {

    private static final String USER_EMAIL_TAKEN_MSG = "User with email %s already exists";

    public UserEmailAlreadyTakenException(String email) {
        super(String.format(USER_EMAIL_TAKEN_MSG, email));
    }
}
