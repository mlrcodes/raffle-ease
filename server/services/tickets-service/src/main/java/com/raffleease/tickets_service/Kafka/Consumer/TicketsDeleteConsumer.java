package com.raffleease.tickets_service.Kafka.Consumer;

import com.raffleease.common_models.DTO.Kafka.TicketsDelete;
import com.raffleease.tickets_service.Tickets.Services.DeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TicketsDeleteConsumer {
    private final DeleteService deleteService;

    @KafkaListener(topics = "tickets-delete-topic", groupId = "tickets-group")
    public void consumeRelease (
            TicketsDelete request
    ) {
        deleteService.delete(request);
    }
}
