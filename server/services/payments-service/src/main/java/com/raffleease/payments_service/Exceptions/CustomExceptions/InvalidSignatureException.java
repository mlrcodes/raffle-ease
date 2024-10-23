package com.raffleease.payments_service.Exceptions.CustomExceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Getter
public class InvalidSignatureException extends RuntimeException {
    private final String msg;
}