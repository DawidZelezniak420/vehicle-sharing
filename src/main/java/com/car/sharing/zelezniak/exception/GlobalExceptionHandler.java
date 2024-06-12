package com.car.sharing.zelezniak.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorInformation> handleException(
            NoSuchElementException exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return createResponse(status, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorInformation> handleException(
            IllegalArgumentException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return createResponse(status, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInformation> handleException(
            Exception exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return createResponse(status, exception.getMessage());
    }

    private ResponseEntity<ErrorInformation> createResponse(
            HttpStatus status,
            String message) {
        return ResponseEntity.status(status)
                .body(new ErrorInformation(
                        message, status.value()));
    }
}
