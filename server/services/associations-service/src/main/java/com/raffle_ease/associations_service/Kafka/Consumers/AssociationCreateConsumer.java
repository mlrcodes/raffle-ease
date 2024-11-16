package com.raffle_ease.associations_service.Kafka.Consumers;

import com.raffle_ease.associations_service.Associations.Services.AssociationsService;
import com.raffleease.common_models.DTO.Kafka.AssociationCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AssociationCreateConsumer {
    private final AssociationsService service;

    @KafkaListener(topics = "association-create-topic", groupId = "associations-group")
    public void consumeAssociationCreate (
            AssociationCreate request
    ) {
        service.create(request);
    }
}