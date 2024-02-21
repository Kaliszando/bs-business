package com.bts.bugstalker.core.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MandatoryEntityNotFoundException extends RuntimeException {

    private static final String DB_RESULT_NOT_FOUND = "Entity %s with id %s was not found in DB";

    public MandatoryEntityNotFoundException(String className, String id) {
        super(String.format(DB_RESULT_NOT_FOUND, className, id));
    }
}
