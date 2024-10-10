package com.raffleease.raffles_service.Tickets.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record PurchaseRequest(

        @NotEmpty(message = "Must purchase at least one ticket")
        Set<Long> ticketsIds,

        @NotNull(message = "Customer purchasing tickets must be identified")
        String customerId
) {}