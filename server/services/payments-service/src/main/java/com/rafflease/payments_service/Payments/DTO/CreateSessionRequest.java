package com.rafflease.payments_service.Payments.DTO;

import jakarta.validation.constraints.NotNull;

public record CreateSessionRequest(
        @NotNull(message = "Must select a raffle")
        Long raffleId,

        @NotNull(message = "Must indicate the number of tickets")
        Long quantity,

        @NotNull(message = "Must indicate an order")
        Long orderId
) {}