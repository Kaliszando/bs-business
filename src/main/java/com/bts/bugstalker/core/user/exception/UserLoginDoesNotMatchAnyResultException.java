package com.bts.bugstalker.core.user.exception;

import com.bts.bugstalker.core.common.exception.base.BusinessException;
import com.bts.bugstalker.core.common.exception.base.ExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserLoginDoesNotMatchAnyResultException extends BusinessException {

    private static final String INVALID_LOGIN_MSG = "No user with login %s found";

    public UserLoginDoesNotMatchAnyResultException(String login) {
        super(ExceptionCode.of("user.login-does-not-match-any-user"),
                String.format(INVALID_LOGIN_MSG, login));
    }
}
