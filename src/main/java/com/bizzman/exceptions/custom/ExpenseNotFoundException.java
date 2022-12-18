package com.bizzman.exceptions.custom;

public class ExpenseNotFoundException extends RuntimeException{

    public ExpenseNotFoundException(String message, Throwable exception) {
        super(message, exception);
    }

    public ExpenseNotFoundException(String message) {
        super(message);
    }
}
