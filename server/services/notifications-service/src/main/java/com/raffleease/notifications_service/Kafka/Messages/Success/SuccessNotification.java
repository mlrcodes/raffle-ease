package com.raffleease.notifications_service.Kafka.Messages.Success;

import jakarta.validation.constraints.NotNull;

public record SuccessNotification(

        @NotNull(message = "Must provide payment data")
        PaymentData paymentData,

        @NotNull(message = "Must provide customer data")
        CustomerDTO customerData,

        @NotNull(message = "Must provide order data")
        OrderData orderData

) {
}