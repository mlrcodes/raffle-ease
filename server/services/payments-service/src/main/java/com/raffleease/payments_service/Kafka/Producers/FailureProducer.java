package com.raffleease.payments_service.Kafka.Producers;

import com.raffleease.common_models.DTO.Kafka.PaymentFailure;
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
public class FailureProducer {
    private final KafkaTemplate<String, PaymentFailure> failureTemplate;

    public void notifyFailure(PaymentFailure request) {
        Message<PaymentFailure> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "payment-failure-topic")
                .build();

        try {
            failureTemplate.send(message);
        } catch (KafkaException exp) {
            throw new CustomKafkaException("Error sending order failure notification: " + exp.getMessage());
        }
    }
}
