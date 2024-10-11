package com.raffleease.orders_service.Orders.DTO;

import com.raffleease.orders_service.Tickets.DTO.TicketDTO;
import lombok.Builder;

import java.util.Set;

@Builder
public record OrderData(
        Long orderId,
        String orderReference,
        Set<TicketDTO> tickets,
        Long orderDate
) {
}