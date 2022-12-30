package com.bizzman.exceptions.custom;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException(String message, Throwable exception) {
        super(message, exception);
    }

    public InvalidRoleException(String message) {
        super(message);
    }
}
