package com.raffleease.raffles_service.Tickets.DTO;

import com.raffleease.raffles_service.Tickets.Models.TicketStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TicketResponse(
        Long id,

        Long raffleId,

        String ticketNumber,

        TicketStatus status,

        String reservationFlag,

        LocalDateTime reservationTime,

        String customerId
) { }