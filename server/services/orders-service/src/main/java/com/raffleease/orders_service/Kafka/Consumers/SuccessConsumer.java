package com.raffleease.orders_service.Kafka.Consumers;


import com.raffleease.common_models.DTO.Kafka.PaymentSuccess;
import com.raffleease.orders_service.Orders.Services.OrderResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SuccessConsumer {
    private final OrderResultService ordersService;

    @KafkaListener(topics = "payment-success-topic", groupId = "orders-group")
    public void consumeSuccess(
            PaymentSuccess request
    ) {
        ordersService.handleOrderSuccess(request);
    }
}
