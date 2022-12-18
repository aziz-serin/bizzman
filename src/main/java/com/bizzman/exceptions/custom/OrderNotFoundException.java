package com.bizzman.exceptions.custom;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String message, Throwable exception) {
        super(message, exception);
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
