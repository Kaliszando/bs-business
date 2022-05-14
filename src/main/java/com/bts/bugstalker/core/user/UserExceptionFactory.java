package com.bts.bugstalker.core.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

abstract class UserExceptionFactory {

    private static final String USER_NOT_FOUND = "No user with id: %o found";
    private static final String USER_EMAIL_TAKEN = "User with email: %s already exists";

    public static UserBusinessException createGeneralException(String message) {
        return new UserBusinessException(message);
    }

    public static UserNotFoundException createUserNotFoundException(Long id) {
        return new UserNotFoundException(String.format(USER_NOT_FOUND, id));
    }

    public static UserEmailIsTakenException createUserEmailIsTakenException(String email) {
        return new UserEmailIsTakenException(String.format(USER_EMAIL_TAKEN, email));
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    private static class UserBusinessException extends RuntimeException {
        private UserBusinessException(String message) {
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public static class UserNotFoundException extends UserBusinessException {
        private UserNotFoundException(String message) {
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public static class UserEmailIsTakenException extends UserBusinessException {
        private UserEmailIsTakenException(String message) {
            super(message);
        }
    }

}