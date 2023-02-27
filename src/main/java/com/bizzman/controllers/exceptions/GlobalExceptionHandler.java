package com.bizzman.controllers.exceptions;

import com.bizzman.exceptions.custom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String NOT_FOUND_MSG = "{ \"error\": \"%s\"}";

    @ExceptionHandler({
            BusinessInformationNotFoundException.class,
            BusinessRelationshipNotFoundException.class,
            EmergencyContactDetailsNotFoundException.class,
            EmployeeNotFoundException.class,
            ExpenseNotFoundException.class,
            OrderNotFoundException.class,
            PersonalDetailsNotFoundException.class,
            ProductNotFoundException.class
    })
    public ResponseEntity<?> handleNotFoundException(Exception exception) {
        String errorMessage = String.format(NOT_FOUND_MSG, exception.getMessage());
        logger.error(errorMessage);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorMessage);
    }

    @ExceptionHandler({
            JdbcSQLIntegrityConstraintViolationException.class
    })
    public ResponseEntity<?> handleConstraintViolationException(Exception exception) {
        String errorMessage = String.format(NOT_FOUND_MSG, exception.getMessage());
        logger.error(errorMessage);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(errorMessage);
    }
}
