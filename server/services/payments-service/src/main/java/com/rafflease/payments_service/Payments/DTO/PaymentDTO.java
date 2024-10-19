package com.rafflease.payments_service.Payments.DTO;

import lombok.Builder;

@Builder
public record PaymentDTO (
        String paymentId,
        Long orderId,
        String paymentIntentId
) {
}