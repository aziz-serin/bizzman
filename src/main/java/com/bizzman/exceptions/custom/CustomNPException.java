package com.bizzman.exceptions.custom;

public class CustomNPException extends RuntimeException{

    public CustomNPException(String message, Throwable exception) {
        super(message, exception);
    }

    public CustomNPException(String message) {
        super(message);
    }
}
