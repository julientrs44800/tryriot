package com.tryriot.common.exception;

import com.tryriot.cryptography.exception.CreateSignatureFailureException;
import com.tryriot.cryptography.exception.DecryptionFailureException;
import com.tryriot.cryptography.exception.EncryptionFailureException;
import com.tryriot.cryptography.exception.PayloadNotAJSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private ResponseEntity<Object> handleException(Exception ex, HttpStatus status) {
        return handleException(ex, status, ex.getMessage());
    }

    private ResponseEntity<Object> handleException(Exception ex, HttpStatus status, String logMessage) {
        return ResponseEntity.status(status).body(ex.getMessage());
    }

    @ExceptionHandler({ PayloadNotAJSONException.class })
    protected ResponseEntity<Object> handlePayloadNotAJSONException(PayloadNotAJSONException ex) {
        return handleException(ex, ex.returnCode);
    }

    @ExceptionHandler({ EncryptionFailureException.class })
    protected ResponseEntity<Object> handleEncryptionFailureException(EncryptionFailureException ex) {
        return handleException(ex, ex.returnCode);
    }

    @ExceptionHandler({ DecryptionFailureException.class })
    protected ResponseEntity<Object> handleDecryptionFailureException(DecryptionFailureException ex) {
        return handleException(ex, ex.returnCode);
    }

    @ExceptionHandler({ CreateSignatureFailureException.class })
    protected ResponseEntity<Object> handleCreateSignatureFailureException(CreateSignatureFailureException ex) {
        return handleException(ex, ex.returnCode);
    }
}
