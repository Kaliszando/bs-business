package com.bts.bugstalker.util.generic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class BusinessExceptionFactory {

    protected static BusinessException createGeneralException(String message) {
        return new BusinessException(message);
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public static class BusinessException extends RuntimeException {
        protected BusinessException(String message) {
            super(message);
        }
    }
}
