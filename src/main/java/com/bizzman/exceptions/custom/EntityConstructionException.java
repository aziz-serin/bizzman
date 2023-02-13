package com.bizzman.exceptions.custom;

public class EntityConstructionException extends RuntimeException {
    public EntityConstructionException(String message, Throwable exception) {
        super(message, exception);
    }

    public EntityConstructionException(String message) {
        super(message);
    }
}
