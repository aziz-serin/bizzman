package com.bizzman.exceptions;

public class CustomNPException extends RuntimeException{

    public CustomNPException(String message, Throwable exception){
        super(message, exception);
    }
}
