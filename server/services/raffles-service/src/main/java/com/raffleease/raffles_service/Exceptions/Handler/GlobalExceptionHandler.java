package com.raffleease.raffles_service.Exceptions.Handler;

import com.raffleease.raffles_service.Exceptions.CustomExceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AssociationRetrievalException.class)
    public ResponseEntity<String> handleAssociationRetrievalException(AssociationRetrievalException exp) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(exp.getMsg());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException exp) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(exp.getMsg());
    }
    @ExceptionHandler(CustomStripeException.class)
    public ResponseEntity<String> HandleCustomStripeException(CustomStripeException exp) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(exp.getMessage());
    }

    @ExceptionHandler(DataBaseHandlingException.class)
    public ResponseEntity<String> handleDataBaseAccessException(DataBaseHandlingException exp) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(exp.getMsg());
    }

    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<String> handlePriceNotFoundException(PriceNotFoundException exp) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(exp.getMsg());
    }

    @ExceptionHandler(RafflesNotFoundException.class)
    public ResponseEntity<String> handleRaffleNotFoundException(RafflesNotFoundException exp) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(exp.getMsg());
    }


    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<String> handleTicketNotFoundException(TicketNotFoundException exp) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(exp.getMsg());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        Map<String, String> errors = new HashMap<>();

        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError)error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(new ErrorResponse(errors));
    }
}