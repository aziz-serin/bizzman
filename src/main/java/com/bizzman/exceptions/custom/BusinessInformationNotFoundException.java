package com.bizzman.exceptions.custom;

public class BusinessInformationNotFoundException extends RuntimeException{

    public BusinessInformationNotFoundException(String message, Throwable exception) {
        super(message, exception);
    }

    public BusinessInformationNotFoundException(String message) {
        super(message);
    }
}
