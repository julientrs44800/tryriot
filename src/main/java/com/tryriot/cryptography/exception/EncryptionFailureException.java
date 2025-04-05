package com.tryriot.cryptography.exception;

import org.springframework.http.HttpStatus;

public class EncryptionFailureException extends RuntimeException {
    public final HttpStatus returnCode = HttpStatus.INTERNAL_SERVER_ERROR;

    public EncryptionFailureException(String message) {
        super(message);
    }
}
