package com.raffleease.orders_service.Kafka.Producers;

import com.raffleease.common_models.DTO.Kafka.TicketsReleaseRequest;
import com.raffleease.common_models.DTO.Tickets.ReservationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReleaseProducer {
    private final KafkaTemplate<String, TicketsReleaseRequest> ticketsReleaseTemplate;


    public void sendTicketsReleaseNotification(TicketsReleaseRequest request) {
        Message<TicketsReleaseRequest> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "tickets-release-topic")
                .build();

        ticketsReleaseTemplate.send(message);
    }
}