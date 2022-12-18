package com.bizzman.exceptions.custom;

public class EmployeeNotFoundException extends RuntimeException{

    public EmployeeNotFoundException(String message, Throwable exception) {
        super(message, exception);
    }

    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
