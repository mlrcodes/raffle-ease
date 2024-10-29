package com.raffleease.orders_service.Kafka.Consumers;

import com.raffleease.common_models.DTO.Kafka.PaymentFailure;
import com.raffleease.orders_service.Orders.Services.OrderResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FailureConsumer {
    private final OrderResultService ordersService;

    @KafkaListener(topics = "payment-failure-topic", groupId = "orders-group")
    public void consumeFailure(
            PaymentFailure request
    ) {
        ordersService.handleOrderFailure(request);
    }
}