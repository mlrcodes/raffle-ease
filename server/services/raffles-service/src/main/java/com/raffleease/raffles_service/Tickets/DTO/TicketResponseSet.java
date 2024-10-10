package com.raffleease.raffles_service.Tickets.DTO;

import lombok.Builder;

import java.util.Set;

@Builder
public record TicketResponseSet(
        Set<TicketResponse> tickets
) {
}