package com.raffleease.raffles_service.Tickets.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record PurchaseRequest(

        @NotNull(message = "Ticket IDs cannot be null")
        @NotEmpty(message = "Ticket IDs cannot be empty")
        Set<Long> ticketsIds,

        @NotNull(message = "Customer ID cannot be null")
        @Size(min = 1, message = "Customer ID cannot be empty")
        String customerId
) {}