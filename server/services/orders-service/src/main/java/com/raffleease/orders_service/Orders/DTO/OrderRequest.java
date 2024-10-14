package com.raffleease.orders_service.Orders.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record OrderRequest (
        @NotNull(message = "Must select a raffle")
        Long raffleId,

        @NotEmpty(message = "Must purchase at least one ticket")
        Set<Long> tickets,

        @NotNull(message = "Reservation flag is required to complete order")
        String reservationFlag
) {
}