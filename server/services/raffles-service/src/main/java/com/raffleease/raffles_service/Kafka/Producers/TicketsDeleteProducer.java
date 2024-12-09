package com.raffleease.raffles_service.Kafka.Producers;

import com.raffleease.common_models.DTO.Kafka.TicketsDelete;
import com.raffleease.common_models.Exceptions.CustomExceptions.CustomKafkaException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TicketsDeleteProducer {
    private final KafkaTemplate<String, TicketsDelete> ticketsDeleteTemplate;

    public void deleteTickets(TicketsDelete ticketsDelete) {
        Message<TicketsDelete> message = MessageBuilder
                .withPayload(ticketsDelete)
                .setHeader(KafkaHeaders.TOPIC, "tickets-delete-topic")
                .build();

        try {
            ticketsDeleteTemplate.send(message);
        } catch (Exception exp) {
            throw new CustomKafkaException("Unexpected error sending kafka message for deleting tickets: " + exp.getMessage());
        }
    }
}