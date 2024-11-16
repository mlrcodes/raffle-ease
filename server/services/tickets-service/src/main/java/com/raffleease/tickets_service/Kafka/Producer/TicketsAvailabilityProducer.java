package com.raffleease.tickets_service.Kafka.Producer;

import com.raffleease.common_models.DTO.Kafka.TicketsAvailability;
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
public class TicketsAvailabilityProducer {
    private final KafkaTemplate<String, TicketsAvailability> availableTicketsTemplate;

    public void modifyAvailability(TicketsAvailability request) {
        Message<TicketsAvailability> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "tickets-availability-topic")
                .build();
        try {
            availableTicketsTemplate.send(message);
        } catch (KafkaException exp) {
            throw new CustomKafkaException("Error sending tickets availability modification message: " + exp.getMessage());
        }
    }
}

