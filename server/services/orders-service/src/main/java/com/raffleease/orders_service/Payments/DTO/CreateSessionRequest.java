package com.raffleease.orders_service.Payments.DTO;

import lombok.Builder;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Builder
public record CreateSessionRequest(
        @NotNull(message = "Raffle ID cannot be null")
        @Min(value = 1, message = "Raffle ID must be greater than 0")
        Long raffleId,

        @NotNull(message = "Amount cannot be null")
        @Min(value = 1, message = "Amount must be greater than 0")
        Long quantity,

        @NotNull(message = "Order ID cannot be null")
        @Min(value = 1, message = "Order ID must be greater than 0")
        Long orderId
) {}
