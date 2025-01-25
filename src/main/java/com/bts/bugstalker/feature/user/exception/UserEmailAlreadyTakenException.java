package com.bts.bugstalker.feature.user.exception;

import com.bts.bugstalker.common.exception.base.BusinessException;
import com.bts.bugstalker.common.exception.base.ExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class UserEmailAlreadyTakenException extends BusinessException {

    private static final String USER_EMAIL_TAKEN_MSG = "User with email %s already exists";

    public UserEmailAlreadyTakenException(String email) {
        super(ExceptionCode.of("user.email-already-taken"),
                String.format(USER_EMAIL_TAKEN_MSG, email));
    }
}
