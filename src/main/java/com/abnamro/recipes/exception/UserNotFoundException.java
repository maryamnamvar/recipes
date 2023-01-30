package com.abnamro.recipes.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userId) {
        super(String.format("User id '%s' not found!!!", userId));
    }
}
