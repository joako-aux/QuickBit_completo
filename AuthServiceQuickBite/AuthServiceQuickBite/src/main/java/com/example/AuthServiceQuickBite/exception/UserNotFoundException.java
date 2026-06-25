package com.example.AuthServiceQuickBite.exception;

public class UserNotFoundException
        extends RuntimeException {

    public UserNotFoundException(
            String message
    ) {
        super(message);
    }
}