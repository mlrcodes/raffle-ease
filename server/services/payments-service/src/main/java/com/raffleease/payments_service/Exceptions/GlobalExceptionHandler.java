package com.raffleease.payments_service.Exceptions;

import com.raffleease.common_models.Exceptions.CustomExceptions.*;
import com.raffleease.common_models.Exceptions.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomDeserializationException.class)
    public ResponseEntity<String> handleCustomDeserializationException(CustomDeserializationException exp) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exp.getMsg());
    }

    @ExceptionHandler(CustomKafkaException.class)
    public ResponseEntity<String> handleCustomKafkaException(CustomKafkaException exp) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exp.getMsg());
    }

    @ExceptionHandler(CustomStripeException.class)
    public ResponseEntity<String> handleCustomStripeException(CustomStripeException exp) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exp.getMsg());
    }

    @ExceptionHandler(DataBaseHandlingException.class)
    public ResponseEntity<String> handleDataBaseHandlingException(DataBaseHandlingException exp) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exp.getMsg());
    }

    @ExceptionHandler(InvalidSignatureException.class)
    public ResponseEntity<String> handleInvalidSignatureException(InvalidSignatureException exp) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exp.getMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        Map<String, String> errors = new HashMap<>();

        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errors));
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> handleObjectNotFoundException(ObjectNotFoundException exp) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exp.getMsg());
    }

    @ExceptionHandler(ObjectRetrievalException.class)
    public ResponseEntity<String> handleObjectRetrievalException(ObjectRetrievalException exp) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exp.getMsg());
    }

    @ExceptionHandler(StripeWebHookException.class)
    public ResponseEntity<String> handleStripeWebHookException(StripeWebHookException exp) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exp.getMsg());
    }
}
