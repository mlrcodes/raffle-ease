package com.raffleease.payments_service.Kafka.Producers;

import com.raffleease.common_models.DTO.Kafka.PaymentSuccess;
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
public class SuccessProducer {
    private final KafkaTemplate<String, PaymentSuccess> successTemplate;

    public void notifySuccess(PaymentSuccess request) {
        Message<PaymentSuccess> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "payment-success-topic")
                .build();

        try {
            successTemplate.send(message);
        } catch (KafkaException exp) {
            throw new CustomKafkaException("Error sending order success notification: " + exp.getMessage());
        }
    }
}
