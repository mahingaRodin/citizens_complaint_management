package com.citizen.engagement_system_be.exceptions;

import java.util.Objects;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.citizen.engagement_system_be.payloads.ErrorResponse;

@ControllerAdvice
public class AppFailureException {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleAnyError(RuntimeException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage(), exception));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidations(MethodArgumentNotValidException exception) {
        FieldError error = Objects.requireNonNull(exception.getFieldError());
        String message = error.getField() + ": " + error.getDefaultMessage();
        return ResponseEntity.badRequest().body(new ErrorResponse(message, error));
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleSqlExceptions(ConstraintViolationException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage() + " - " + exception.getSQL() + " - " + exception.getSQLState(), exception.getSQLException()));
    }
}
