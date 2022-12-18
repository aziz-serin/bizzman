package com.bizzman.exceptions.custom;

public class InvalidExpenseException extends RuntimeException{

    public InvalidExpenseException(String message, Throwable exception) {
        super(message, exception);
    }

    public InvalidExpenseException(String message) {
        super(message);
    }
}
