package com.raffleease.raffles_service.Tickets.DTO;

import com.raffleease.raffles_service.Tickets.Models.TicketStatus;
import lombok.Builder;

@Builder
public record TicketResponse(
        Long id,

        Long raffleId,

        String ticketNumber,

        TicketStatus status,

        String customerId
) { }