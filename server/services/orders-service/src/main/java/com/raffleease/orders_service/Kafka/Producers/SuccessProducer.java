package com.raffleease.orders_service.Kafka.Producers;

import com.raffleease.common_models.DTO.Kafka.OrderSuccess;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SuccessProducer {
    private final KafkaTemplate<String, OrderSuccess> successNotificationTemplate;

    public void sendSuccessNotification(OrderSuccess request) {
        Message<OrderSuccess> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "order-success-topic")
                .build();

        successNotificationTemplate.send(message);
    }
}