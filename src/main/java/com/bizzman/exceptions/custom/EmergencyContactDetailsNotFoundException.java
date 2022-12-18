package com.bizzman.exceptions.custom;

public class EmergencyContactDetailsNotFoundException extends RuntimeException {

    public EmergencyContactDetailsNotFoundException(String message, Throwable exception) {
        super(message, exception);
    }

    public EmergencyContactDetailsNotFoundException(String message) {
        super(message);
    }
}
