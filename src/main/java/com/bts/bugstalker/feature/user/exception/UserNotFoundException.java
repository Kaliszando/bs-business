package com.bts.bugstalker.feature.user.exception;

import com.bts.bugstalker.common.exception.base.BusinessException;
import com.bts.bugstalker.common.exception.base.ExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends BusinessException {

    private static final String USER_WITH_ID_NOT_FOUND_MSG = "No user with id: %d found";
    private static final String USER_WITH_USERNAME_NOT_FOUND_MSG = "No user with login: %s found";

    public UserNotFoundException(Long id) {
        super(ExceptionCode.of("user.no-user-with-given-id"),
                String.format(USER_WITH_ID_NOT_FOUND_MSG, id));
    }

    public UserNotFoundException(String username) {
        super(ExceptionCode.of("user.no-user-with-given-username"),
                String.format(USER_WITH_USERNAME_NOT_FOUND_MSG, username));
    }
}
