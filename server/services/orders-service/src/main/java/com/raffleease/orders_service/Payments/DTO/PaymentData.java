package com.raffleease.orders_service.Payments.DTO;

import lombok.Builder;
@Builder
public record PaymentData(

        String paymentMethod,

        Double total
) {
}