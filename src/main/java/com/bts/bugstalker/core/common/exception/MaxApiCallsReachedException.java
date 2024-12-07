package com.bts.bugstalker.core.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class MaxApiCallsReachedException extends RuntimeException {

    private static final String MAX_API_CALLS_REACHED = "Reached max api call limit of %d for %s";

    public MaxApiCallsReachedException(String key, int limit) {
        super(String.format(MAX_API_CALLS_REACHED, limit, key));
    }
}
