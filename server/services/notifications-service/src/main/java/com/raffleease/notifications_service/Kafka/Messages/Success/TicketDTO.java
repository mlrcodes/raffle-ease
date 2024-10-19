package com.raffleease.notifications_service.Kafka.Messages.Success;

import jakarta.validation.constraints.NotNull;

public record TicketDTO(
        Long id,
        String ticketNumber
) {
}