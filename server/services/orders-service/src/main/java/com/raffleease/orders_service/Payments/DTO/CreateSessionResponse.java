package com.raffleease.orders_service.Payments.DTO;

import lombok.Builder;

@Builder
public record CreateSessionResponse(
        String clientSecret
) {
}