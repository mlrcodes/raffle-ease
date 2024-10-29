package com.raffleease.orders_service.Exceptions;

import com.raffleease.common_models.Exceptions.CustomExceptions.CheckoutException;
import com.raffleease.common_models.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.common_models.Exceptions.CustomExceptions.NotificationException;
import com.raffleease.common_models.Exceptions.CustomExceptions.ObjectNotFoundException;
import com.raffleease.common_models.Exceptions.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> HandleObjectNotFoundException(ObjectNotFoundException exp) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exp.getMsg());
    }

    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<String> handleNotificationException(NotificationException exp) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exp.getMsg());
    }

    @ExceptionHandler(CheckoutException.class)
    public ResponseEntity<String> HandlePaymentProcessException(CheckoutException exp) {
        return ResponseEntity
                .status(HttpStatus.PAYMENT_REQUIRED)
                .body(exp.getMsg());
    }

    @ExceptionHandler(DataBaseHandlingException.class)
    public ResponseEntity<String> handleDataBaseAccessException(DataBaseHandlingException exp) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
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
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse(errors));
    }
}
