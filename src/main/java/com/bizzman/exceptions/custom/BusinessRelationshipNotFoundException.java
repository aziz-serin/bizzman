package com.bizzman.exceptions.custom;

public class BusinessRelationshipNotFoundException extends RuntimeException {

    public BusinessRelationshipNotFoundException(String message, Throwable exception) {
        super(message, exception);
    }

    public BusinessRelationshipNotFoundException(String message) {
        super(message);
    }
}
