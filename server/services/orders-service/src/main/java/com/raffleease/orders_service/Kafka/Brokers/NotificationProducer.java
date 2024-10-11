package com.raffleease.orders_service.Kafka.Brokers;

import com.raffleease.orders_service.Kafka.Messages.Success.SuccessNotificationRequest;
import com.raffleease.orders_service.Kafka.Messages.Tickets.TicketsReleaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class NotificationProducer {

    private final KafkaTemplate<String, SuccessNotificationRequest> successNotificationKafkaTemplate;
    private final KafkaTemplate<String, SuccessNotificationRequest> ticketsReleaseKafkaTemplate;

    public void sendSuccessNotification(SuccessNotificationRequest request) {
        Message<SuccessNotificationRequest> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "n-success-topic")
                .build();

        successNotificationKafkaTemplate.send(message);
    }

    public void sendTicketsReleaseNotification(TicketsReleaseRequest request) {
        Message<TicketsReleaseRequest> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "release-tickets-topic")
                .build();

        ticketsReleaseKafkaTemplate.send(message);
    }
}