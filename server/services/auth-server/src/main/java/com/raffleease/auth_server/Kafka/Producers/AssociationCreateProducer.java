package com.raffleease.auth_server.Kafka.Producers;

import com.raffleease.common_models.DTO.Kafka.AssociationCreate;
import com.raffleease.common_models.DTO.Kafka.TicketsDelete;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AssociationCreateProducer {
    private final KafkaTemplate<String, AssociationCreate> associationTemplate;

    public void createAssociation(AssociationCreate association) {
        Message<AssociationCreate> message = MessageBuilder
                .withPayload(association)
                .setHeader(KafkaHeaders.TOPIC, "association-create-topic")
                .build();

        associationTemplate.send(message);
    }
}