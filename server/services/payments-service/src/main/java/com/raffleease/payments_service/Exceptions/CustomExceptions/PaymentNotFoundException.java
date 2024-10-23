package com.raffleease.payments_service.Exceptions.CustomExceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
public class PaymentNotFoundException extends Throwable {
    private final String msg;
}