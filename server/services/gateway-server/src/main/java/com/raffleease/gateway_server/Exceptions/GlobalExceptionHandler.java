package com.raffleease.gateway_server.Exceptions;

import com.raffleease.common_models.Exceptions.ApiError;
import com.raffleease.common_models.Exceptions.CustomExceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.ServiceUnavailableException;
import java.sql.Timestamp;
import java.time.Instant;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ApiError> handleAuthorizationException(AuthorizationException exp) {
        ApiError apiError = ApiError.builder()
                .code(UNAUTHORIZED.value())
                .reason(UNAUTHORIZED.getReasonPhrase())
                .message(exp.getMessage())
                .timestamp(Timestamp.from(Instant.now()))
                .build();

        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(apiError);
    }


    @ExceptionHandler(CustomIOException.class)
    public ResponseEntity<ApiError> handleCustomIOException(CustomIOException ex) {
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .reason(INTERNAL_SERVER_ERROR.getReasonPhrase())
                .code(INTERNAL_SERVER_ERROR.value())
                .timestamp(Timestamp.from(Instant.now()))
                .build();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ApiError> handleServiceUnavailableException(ServiceUnavailableException exp) {
        ApiError apiError = ApiError.builder()
                .message(exp.getMessage())
                .reason(SERVICE_UNAVAILABLE.getReasonPhrase())
                .code(SERVICE_UNAVAILABLE.value())
                .timestamp(Timestamp.from(Instant.now()))
                .build();
        return ResponseEntity.status(SERVICE_UNAVAILABLE).body(apiError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException exp) {
        ApiError apiError = ApiError.builder()
                .code(BAD_REQUEST.value())
                .reason(BAD_REQUEST.getReasonPhrase())
                .message(exp.getMessage())
                .timestamp(Timestamp.from(Instant.now()))
                .build();

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception exp) {
        ApiError apiError = ApiError.builder()
                .message(exp.getMessage())
                .reason(INTERNAL_SERVER_ERROR.getReasonPhrase())
                .code(INTERNAL_SERVER_ERROR.value())
                .timestamp(Timestamp.from(Instant.now()))
                .build();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(apiError);
    }
}