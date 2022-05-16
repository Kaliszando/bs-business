package com.bts.bugstalker.core.user;

import com.bts.bugstalker.util.generic.BusinessExceptionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ResponseStatus;

abstract class UserExceptionFactory extends BusinessExceptionFactory {

    private static final String USER_NOT_FOUND_MSG = "No user with id \"%o\" found";
    private static final String USER_EMAIL_TAKEN_MSG = "User with email \"%s\" already exists";
    private static final String INVALID_LOGIN_INPUT_MSG = "No user with login \"%s\" found";

    public static UserNotFoundException createUserNotFoundException(Long id) {
        return new UserNotFoundException(String.format(USER_NOT_FOUND_MSG, id));
    }

    public static UserEmailIsTakenException createUserEmailIsTakenException(String email) {
        return new UserEmailIsTakenException(String.format(USER_EMAIL_TAKEN_MSG, email));
    }

    public static LoginInputDoesNotMatchAnyUser createLoginInputDoesNotMatchAnyUserException(String login) {
        return new LoginInputDoesNotMatchAnyUser(String.format(INVALID_LOGIN_INPUT_MSG, login));
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    static class UserNotFoundException extends BusinessException {
        private UserNotFoundException(String message) {
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    static class UserEmailIsTakenException extends BusinessException {
        private UserEmailIsTakenException(String message) {
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    static class LoginInputDoesNotMatchAnyUser extends UsernameNotFoundException {
        private LoginInputDoesNotMatchAnyUser(String message) {
            super(message);
        }
    }

}