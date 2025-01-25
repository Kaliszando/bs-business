package com.bts.bugstalker.common.exception.base;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private ExceptionCode code;

    protected BusinessException(ExceptionCode code, String message) {
        super(message);
        this.code = code;
    }
}
