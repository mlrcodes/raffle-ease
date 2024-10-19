package com.raffleease.raffles_service.Tickets.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SearchRequest(
        @NotNull(message = "Must provide a ticket number")
        String ticketNumber,

        @NotNull(message = "Must indicate the raffle")
        Long raffleId
) {}