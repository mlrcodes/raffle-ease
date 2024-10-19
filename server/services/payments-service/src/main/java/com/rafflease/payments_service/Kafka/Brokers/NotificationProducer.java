package com.rafflease.payments_service.Kafka.Brokers;

import com.rafflease.payments_service.Exceptions.CustomExceptions.CustomKafkaException;
import com.rafflease.payments_service.Kafka.Messages.FailureNotificationRequest;
import com.rafflease.payments_service.Kafka.Messages.SuccessNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationProducer {
    private final KafkaTemplate<String, SuccessNotificationRequest> paymentSuccessKafkaTemplate;
    private final KafkaTemplate<String, FailureNotificationRequest> paymentFailureKafkaTemplate;

    public void sendSuccessNotification(SuccessNotificationRequest request) {
        Message<SuccessNotificationRequest> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "p-success-topic")
                .build();

        try {
            paymentSuccessKafkaTemplate.send(message);
        } catch (KafkaException exp) {
            throw new CustomKafkaException("Error sending order success notification: " + exp.getMessage());
        }
    }

    public void sendFailureNotification(FailureNotificationRequest request) {
        Message<FailureNotificationRequest> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "failure-topic")
                .build();

        try {
            paymentFailureKafkaTemplate.send(message);
        } catch (KafkaException exp) {
            throw new CustomKafkaException("Error sending order failure notification: " + exp.getMessage());
        }
    }

}