package com.raffleease.orders_service.Kafka.Brokers.Consumers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SuccessConsumer {

    /*
    private final OrderResultService ordersService;

    @KafkaListener(topics = "p-success-topic", groupId = "orders-group")
    public void consumeSuccess(
            SuccessNotification request
    ) {
        ordersService.handleOrderSuccess(request);
    }

     */
}
