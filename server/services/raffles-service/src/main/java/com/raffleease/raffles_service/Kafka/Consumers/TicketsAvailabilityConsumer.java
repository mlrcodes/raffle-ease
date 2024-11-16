package com.raffleease.raffles_service.Kafka.Consumers;

import com.raffleease.common_models.DTO.Kafka.TicketsAvailability;
import com.raffleease.raffles_service.Raffles.Services.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TicketsAvailabilityConsumer {
    private final AvailabilityService service;

    @KafkaListener(topics = "tickets-release-topic", groupId = "raffles-group")
    public void consumeTicketsAvailability (
            TicketsAvailability request
    ) {
        service.modifyTicketsAvailability(request);
    }
}
