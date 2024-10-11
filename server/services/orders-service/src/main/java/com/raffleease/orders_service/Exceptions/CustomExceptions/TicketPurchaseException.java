package com.raffleease.orders_service.Exceptions.CustomExceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
public class TicketPurchaseException extends RuntimeException {
    private final String msg;
}