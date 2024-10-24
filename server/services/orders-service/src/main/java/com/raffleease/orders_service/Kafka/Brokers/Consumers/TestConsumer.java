package com.raffleease.orders_service.Kafka.Brokers.Consumers;

import com.raffleease.common_models.DTO.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TestConsumer {
    private static final Logger logger = LoggerFactory.getLogger(TestConsumer.class);

    @KafkaListener(topics = "test-topic", groupId = "orders-group")
    public void listen(MessageDTO message) {
        logger.info("Mensaje recibido: " + message);
    }
}
