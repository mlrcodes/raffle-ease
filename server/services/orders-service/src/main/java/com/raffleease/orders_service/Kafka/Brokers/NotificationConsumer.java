package com.raffleease.orders_service.Kafka.Brokers;

import com.raffleease.orders_service.Kafka.Messages.Failure.FailureNotification;
import com.raffleease.orders_service.Kafka.Messages.Success.SuccessNotification;
import com.raffleease.orders_service.Orders.Services.OrderResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationConsumer {

    private final OrderResultService ordersService;

    @KafkaListener(topics = "p-success-topic", groupId = "orders-group")
    public void consumeSuccessNotification(
            SuccessNotification request
    ) {
        ordersService.handleOrderSuccess(request);
    }

    @KafkaListener(topics = "failure-topic", groupId = "orders-group")
    public void consumeFailureNotification(
            FailureNotification request
    ) {
        ordersService.handleOrderFailure(request);
    }
}