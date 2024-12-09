package com.raffleease.notifications_service.Exceptions;

import com.raffleease.common_models.Exceptions.ApiError;
import com.raffleease.common_models.Exceptions.ValidationErrors;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MailException.class)
    public ResponseEntity<ApiError> handleMailException(MailException ex) {
        ApiError apiError = ApiError.builder()
                .message("An unexpected error occurred: " + ex.getMessage())
                .reason(INTERNAL_SERVER_ERROR.getReasonPhrase())
                .code(INTERNAL_SERVER_ERROR.value())
                .timestamp(Timestamp.from(Instant.now()))
                .build();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(apiError);
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
}
