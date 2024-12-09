package com.raffleease.payments_service.Exceptions;

import com.raffleease.common_models.Exceptions.ApiError;
import com.raffleease.common_models.Exceptions.CustomExceptions.*;
import com.raffleease.common_models.Exceptions.ValidationErrors;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomDeserializationException.class)
    public ResponseEntity<ApiError> handleCustomDeserializationException(CustomDeserializationException exp) {
        ApiError apiError = ApiError.builder()
                .code(UNPROCESSABLE_ENTITY.value())
                .reason(UNPROCESSABLE_ENTITY.getReasonPhrase())
                .message(exp.getMessage())
                .timestamp(Timestamp.from(Instant.now()))
                .build();

        return ResponseEntity
                .status(UNPROCESSABLE_ENTITY)
                .body(apiError);
    }

    @ExceptionHandler(CustomKafkaException.class)
    public ResponseEntity<ApiError> handleCustomKafkaException(CustomKafkaException exp) {
        ApiError apiError = ApiError.builder()
                .code(SERVICE_UNAVAILABLE.value())
                .reason(SERVICE_UNAVAILABLE.getReasonPhrase())
                .message(exp.getMessage())
                .timestamp(Timestamp.from(Instant.now()))
                .build();

        return ResponseEntity
                .status(SERVICE_UNAVAILABLE)
                .body(apiError);
    }

    @ExceptionHandler(CustomStripeException.class)
    public ResponseEntity<ApiError> handleCustomStripeException(CustomStripeException exp) {
        ApiError apiError = ApiError.builder()
                .code(BAD_GATEWAY.value())
                .reason(BAD_GATEWAY.getReasonPhrase())
                .message(exp.getMessage())
                .timestamp(Timestamp.from(Instant.now()))
                .build();

        return ResponseEntity
                .status(BAD_GATEWAY)
                .body(apiError);
    }

    @ExceptionHandler(InvalidSignatureException.class)
    public ResponseEntity<ApiError> handleInvalidSignatureException(InvalidSignatureException exp) {
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

    @ExceptionHandler(StripeWebHookException.class)
    public ResponseEntity<ApiError> handleStripeWebHookException(StripeWebHookException exp) {
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
