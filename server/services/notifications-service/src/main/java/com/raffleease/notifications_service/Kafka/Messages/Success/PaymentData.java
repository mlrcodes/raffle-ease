package com.raffleease.notifications_service.Kafka.Messages.Success;

public record PaymentData(

        String paymentMethod,

        Double total
) {
}