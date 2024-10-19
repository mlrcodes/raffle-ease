package com.rafflease.payments_service.Kafka.Messages;

import com.rafflease.payments_service.Orders.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record FailureNotificationRequest(
        @NotNull(message = "Must indicate order")
        Long orderId,

        @NotNull(message = "Must indicate new order status")
        OrderStatus status
) {
}