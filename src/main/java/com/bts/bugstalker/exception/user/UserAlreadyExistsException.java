package com.bts.bugstalker.exception.user;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }

    public UserAlreadyExistsException() {
        super();
    }
}