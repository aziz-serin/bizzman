package com.bizzman.exceptions.custom;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(String message, Throwable exception) {
        super(message, exception);
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
