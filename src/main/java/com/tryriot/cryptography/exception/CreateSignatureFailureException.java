package com.tryriot.cryptography.exception;

import org.springframework.http.HttpStatus;

public class CreateSignatureFailureException extends RuntimeException {
    public final HttpStatus returnCode = HttpStatus.INTERNAL_SERVER_ERROR;

    public CreateSignatureFailureException(String message) {
        super(message);
    }
}

