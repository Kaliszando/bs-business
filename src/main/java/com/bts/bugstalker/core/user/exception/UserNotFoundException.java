package com.bts.bugstalker.core.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    private static final String USER_WITH_ID_NOT_FOUND_MSG = "No user with id: %d found";
    private static final String USER_WITH_USERNAME_NOT_FOUND_MSG = "No user with login: %s found";

    public UserNotFoundException(Long id) {
        super(String.format(USER_WITH_ID_NOT_FOUND_MSG, id));
    }

    public UserNotFoundException(String username) {
        super(String.format(USER_WITH_USERNAME_NOT_FOUND_MSG, username));
    }
}
