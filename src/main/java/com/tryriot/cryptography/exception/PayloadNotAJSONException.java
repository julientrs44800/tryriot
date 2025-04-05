package com.tryriot.cryptography.exception;

import org.springframework.http.HttpStatus;

public class PayloadNotAJSONException extends RuntimeException {
    public final HttpStatus returnCode = HttpStatus.UNPROCESSABLE_ENTITY;

    public PayloadNotAJSONException(String message) {
        super(message);
    }
}
