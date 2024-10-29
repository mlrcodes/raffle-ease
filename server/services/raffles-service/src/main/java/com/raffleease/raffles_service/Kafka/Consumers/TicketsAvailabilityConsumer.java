package com.raffleease.raffles_service.Kafka.Consumers;

import com.raffleease.common_models.DTO.Kafka.TicketsAvailabilityRequest;
import com.raffleease.raffles_service.Raffles.Services.RafflesService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TicketsAvailabilityConsumer {
    private final RafflesService service;

    @KafkaListener(topics = "tickets-release-topic", groupId = "raffles-group")
    public void consumeTicketsAvailability (
            TicketsAvailabilityRequest request
    ) {
        service.modifyTicketsAvailability(request);
    }
}
