package com.raffle_ease.associations_service.Exceptions;

import com.raffleease.common_models.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.common_models.Exceptions.CustomExceptions.ObjectNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataBaseHandlingException.class)
    public ResponseEntity<String> HandleDataBaseAccessException(DataBaseHandlingException exp) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(exp.getMsg());
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> HandleCustomerNotFoundException(ObjectNotFoundException exp) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(exp.getMsg());
    }

}