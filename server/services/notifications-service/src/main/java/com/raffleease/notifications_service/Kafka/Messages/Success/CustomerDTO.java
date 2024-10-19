package com.raffleease.notifications_service.Kafka.Messages.Success;

public record CustomerDTO(
        Long id,
        String name,
        String email,
        String phoneNumber
) {
}