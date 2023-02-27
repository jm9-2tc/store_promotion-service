package com.store.promotionservice.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class RequestProcessingException extends RuntimeException {
    @Getter
    private final HttpStatus status;

    public RequestProcessingException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
