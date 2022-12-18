package com.bizzman.exceptions.custom;

public class PersonalDetailsNotFoundException extends RuntimeException {

    public PersonalDetailsNotFoundException(String message, Throwable exception) {
        super(message, exception);
    }

    public PersonalDetailsNotFoundException(String message) {
        super(message);
    }
}
