package com.raffleease.orders_service.Kafka.Producers;

import com.raffleease.common_models.DTO.Kafka.PurchaseStatistics;
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
public class StatisticsProducer {
    private final KafkaTemplate<String, PurchaseStatistics> statisticsTemplate;

    public void updateStatistics(PurchaseStatistics statistics) {
        Message<PurchaseStatistics> message = MessageBuilder
                .withPayload(statistics)
                .setHeader(KafkaHeaders.TOPIC, "purchase-statistics-topic")
                .build();

        try {
            statisticsTemplate.send(message);
        } catch (KafkaException exp) {
            throw new CustomKafkaException("Unexpected error sending update statistics message: " + exp.getMessage());
        }
    }
}