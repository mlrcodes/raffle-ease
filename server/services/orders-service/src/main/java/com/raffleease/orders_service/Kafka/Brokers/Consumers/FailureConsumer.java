package com.raffleease.orders_service.Kafka.Brokers.Consumers;

import com.raffleease.orders_service.Kafka.Messages.Failure.FailureNotification;
import com.raffleease.orders_service.Orders.Services.OrderResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FailureConsumer {

    /*
    private final OrderResultService ordersService;

    @KafkaListener(topics = "failure-topic", groupId = "orders-group")
    public void consumeFailure(
            FailureNotification request
    ) {
        ordersService.handleOrderFailure(request);
    }

     */
}