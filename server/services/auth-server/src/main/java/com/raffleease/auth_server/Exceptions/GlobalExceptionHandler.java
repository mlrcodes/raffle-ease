package com.raffleease.auth_server.Exceptions;

import com.raffleease.common_models.Exceptions.CustomExceptions.AuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<String> HandleAuthException(AuthException exp) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(exp.getMsg());
    }
}