package com.raffleease.payments_service.Payments.DTO;

import lombok.Builder;

@Builder
public record PaymentDTO (
        String paymentId,
        Long orderId,
        String paymentIntentId
) {
}