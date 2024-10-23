package com.raffleease.orders_service.Payments.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentData(

        @NotEmpty
        String paymentMethod,

        @NotNull
        @Positive
        BigDecimal total
) { }
