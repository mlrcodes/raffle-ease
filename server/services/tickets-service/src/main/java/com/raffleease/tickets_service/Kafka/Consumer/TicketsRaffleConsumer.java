package com.raffleease.tickets_service.Kafka.Consumer;

import com.raffleease.common_models.DTO.Kafka.TicketsRaffle;
import com.raffleease.tickets_service.Tickets.Services.TicketsService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TicketsRaffleConsumer {
    private final TicketsService ticketsService;

    @KafkaListener(topics = "tickets-raffle-topic", groupId = "tickets-group")
    public void consumeTicketsRaffle (
            TicketsRaffle request
    ) {
        ticketsService.setRaffle(request);
    }
}