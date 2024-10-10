package com.raffle_ease.associations_service.Exceptions.Handler;

import com.raffle_ease.associations_service.Exceptions.CustomExceptions.AssociationNotFoundException;
import com.raffle_ease.associations_service.Exceptions.CustomExceptions.DataBaseAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataBaseAccessException.class)
    public ResponseEntity<String> HandleDataBaseAccessException(DataBaseAccessException exp) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(exp.getMsg());
    }

    @ExceptionHandler(AssociationNotFoundException.class)
    public ResponseEntity<String> HandleCustomerNotFoundException(AssociationNotFoundException exp) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(exp.getMsg());
    }

}