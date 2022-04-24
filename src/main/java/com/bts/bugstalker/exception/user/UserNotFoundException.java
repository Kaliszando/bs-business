package com.bts.bugstalker.exception.user;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public UserNotFoundException() {
        super();
    }
}
