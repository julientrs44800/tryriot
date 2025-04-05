package com.tryriot.cryptography.exception;

import org.springframework.http.HttpStatus;

public class DecryptionFailureException extends RuntimeException {
    public final HttpStatus returnCode = HttpStatus.INTERNAL_SERVER_ERROR;

    public DecryptionFailureException(String message) {
        super(message);
    }
}
