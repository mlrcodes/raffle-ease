package com.raffleease.payments_service.Exceptions.CustomExceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
public class DataBaseHandlingException extends RuntimeException {
    private final String msg;
}