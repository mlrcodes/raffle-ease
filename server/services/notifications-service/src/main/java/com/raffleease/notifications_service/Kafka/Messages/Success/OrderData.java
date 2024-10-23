package com.raffleease.notifications_service.Kafka.Messages.Success;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;

@Builder
public record OrderData(

        @Builder
        @NotNull(message = "Order ID cannot be null")
        Long orderId,

        @NotNull(message = "Order reference cannot be null")
        @Size(min = 20, max = 40, message = "Amount of characters not valid")
        String orderReference,

        @NotNull(message = "Tickets set cannot be null")
        @Size(min = 1, message = "There must be at least one ticket associated with the order")
        Set<TicketDTO> tickets,

        @NotNull(message = "Order date cannot be null")
        @PastOrPresent(message = "Order date cannot be in the future")
        Long orderDate
) {}
