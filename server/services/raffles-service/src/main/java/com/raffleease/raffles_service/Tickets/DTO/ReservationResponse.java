package com.raffleease.raffles_service.Tickets.DTO;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record ReservationResponse(
        Set<TicketResponse> tickets,

        String reservationFlag,

        LocalDateTime reservationResponse
) { }
