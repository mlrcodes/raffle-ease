package com.raffleease.notifications_service.Kafka.Messages.Success;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentData(

        @NotEmpty
        String paymentMethod,

        @NotNull
        @Positive
        BigDecimal total
) {
}