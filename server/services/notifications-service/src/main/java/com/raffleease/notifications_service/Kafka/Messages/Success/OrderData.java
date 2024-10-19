package com.raffleease.notifications_service.Kafka.Messages.Success;

import java.util.Set;

public record OrderData(
        Long orderId,
        String orderReference,
        Set<TicketDTO> tickets,
        Long orderDate
) {
}