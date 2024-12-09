package com.raffleease.auth_server.Exceptions;

import com.raffleease.common_models.Exceptions.ApiError;
import com.raffleease.common_models.Exceptions.CustomExceptions.*;
import com.raffleease.common_models.Exceptions.ValidationErrors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ApiError> handleAuthorizationException(AuthorizationException exp) {
        ApiError apiError =ApiError.builder()
                .code(UNAUTHORIZED.value())
                .reason(UNAUTHORIZED.getReasonPhrase())
                .message(exp.getMessage())
                .timestamp(Timestamp.from(Instant.now()))
                .build();

        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(apiError);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException exp) {
        ApiError apiError = ApiError.builder()
                .code(FORBIDDEN.value())
                .reason(FORBIDDEN.getReasonPhrase())
                .message(exp.getMessage())
                .timestamp(Timestamp.from(Instant.now()))
                .build();

        return ResponseEntity
                .status(FORBIDDEN)
                .body(apiError);
    }

    @ExceptionHandler(DataBaseHandlingException.class)
    public ResponseEntity<ApiError> HandleDataBaseAccessException(DataBaseHandlingException exp) {
        ApiError apiError =ApiError.builder()
                .code(INTERNAL_SERVER_ERROR.value())
                .reason(INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(exp.getMessage())
                .timestamp(Timestamp.from(Instant.now()))
                .build();

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(apiError);
    }


    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ApiError> handleObjectNotFoundException(ObjectNotFoundException exp) {
        ApiError apiError =ApiError.builder()
                .code(NOT_FOUND.value())
                .reason(NOT_FOUND.getReasonPhrase())
                .message(exp.getMessage())
                .timestamp(Timestamp.from(Instant.now()))
                .build();

        return ResponseEntity
                .status(NOT_FOUND)
                .body(apiError);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNameNotFoundException(UsernameNotFoundException exp) {
        ApiError apiError =ApiError.builder()
                .code(NOT_FOUND.value())
                .reason(NOT_FOUND.getReasonPhrase())
                .message(exp.getMessage())
                .timestamp(Timestamp.from(Instant.now()))
                .build();

        return ResponseEntity
                .status(NOT_FOUND)
                .body(apiError);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflictException(ConflictException exp) {
        ApiError apiError =ApiError.builder()
                .code(CONFLICT.value())
                .reason(CONFLICT.getReasonPhrase())
                .message(exp.getMessage())
                .timestamp(Timestamp.from(Instant.now()))
                .build();

        return ResponseEntity
                .status(CONFLICT)
                .body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrors> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        Map<String, String> errors = new HashMap<>();

        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError)error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        ValidationErrors errorResponse = new ValidationErrors();
        errorResponse.setErrors(errors);
        errorResponse.setMessage(exp.getMessage());
        errorResponse.setCode(BAD_REQUEST.value());
        errorResponse.setReason(BAD_REQUEST.getReasonPhrase());
        errorResponse.setTimestamp(Timestamp.from(Instant.now()));

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        ApiError apiError = ApiError.builder()
                .message("An unexpected error occurred: " + ex.getMessage())
                .reason(INTERNAL_SERVER_ERROR.getReasonPhrase())
                .code(INTERNAL_SERVER_ERROR.value())
                .timestamp(Timestamp.from(Instant.now()))
                .build();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(BusinessException exp) {
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

    @ExceptionHandler(CustomIOException.class)
    public ResponseEntity<ApiError> handleCustomIOException(CustomIOException ex) {
        ApiError apiError = ApiError.builder()
                .message("An unexpected error occurred: " + ex.getMessage())
                .reason(INTERNAL_SERVER_ERROR.getReasonPhrase())
                .code(INTERNAL_SERVER_ERROR.value())
                .timestamp(Timestamp.from(Instant.now()))
                .build();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(apiError);
    }
}