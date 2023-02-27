package com.store.promotionservice.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RequestProcessingException.class)
    public ResponseEntity<String> generateNotFoundException(RequestProcessingException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
}