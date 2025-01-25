package com.bts.bugstalker.common.exception;

import com.bts.bugstalker.common.exception.base.BusinessException;
import com.bts.bugstalker.common.exception.base.ExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class MaxApiCallsReachedException extends BusinessException {

    private static final String MAX_API_CALLS_REACHED = "Reached max api call limit of %d for %s";

    public MaxApiCallsReachedException(String key, int limit) {
        super(ExceptionCode.of("core.api-call-limit-reached"),
                String.format(MAX_API_CALLS_REACHED, limit, key));
    }
}
