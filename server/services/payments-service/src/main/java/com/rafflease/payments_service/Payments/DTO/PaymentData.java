package com.rafflease.payments_service.Payments.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Builder
public record PaymentData(

        @NotEmpty
        String paymentMethod,

        @NotNull
        @Positive
        BigDecimal total
) { }
