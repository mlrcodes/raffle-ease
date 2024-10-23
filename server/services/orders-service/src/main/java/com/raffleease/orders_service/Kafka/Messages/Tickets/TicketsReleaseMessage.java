package com.raffleease.orders_service.Kafka.Messages.Tickets;

import lombok.Builder;
import java.util.Set;

@Builder
public record TicketsReleaseMessage(
        Set<Long> ticketsIds
) {
}