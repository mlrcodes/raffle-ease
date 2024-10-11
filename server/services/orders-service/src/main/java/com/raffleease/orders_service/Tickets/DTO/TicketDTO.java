package com.raffleease.orders_service.Tickets.DTO;

import com.raffleease.orders_service.Tickets.Model.TicketStatus;
import lombok.Builder;

@Builder
public record TicketDTO(
        Long id,

        Long raffleId,

        String ticketNumber,

        TicketStatus status,

        String customerId
) { }