package com.raffleease.orders_service.Kafka.Messages.Failure;


import com.raffleease.orders_service.Orders.Models.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record FailureNotification(
        @NotNull(message = "Must indicate order")
        Integer orderId,

        @NotNull(message = "Must indicate new order status")
        OrderStatus status
) {
}