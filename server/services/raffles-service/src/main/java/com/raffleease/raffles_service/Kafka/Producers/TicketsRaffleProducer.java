package com.raffleease.raffles_service.Kafka.Producers;

import com.raffleease.common_models.DTO.Kafka.TicketsRaffleRequest;
import com.raffleease.common_models.Exceptions.CustomExceptions.CustomKafkaException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TicketsRaffleProducer {
    private final KafkaTemplate<String, TicketsRaffleRequest> raffleIdTemplate;

    public void produceTicketsRaffle(TicketsRaffleRequest request) {
        Message<TicketsRaffleRequest> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "tickets-raffle-topic")
                .build();

        try {
            raffleIdTemplate.send(message);
        } catch (KafkaException exp) {
            throw new CustomKafkaException("Error sending tickets availability modification message: " + exp.getMessage());
        }
    }
}