package com.raffleease.payments_service.Kafka.Brokers.Producers;

import com.raffleease.common_models.DTO.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TestProducer {

    private final KafkaTemplate<String, MessageDTO> testTemplate;

    public String sendMessage(MessageDTO message) {
        try {
            testTemplate.send("test-topic", message);
            return "Success";
        } catch (KafkaException exp) {
            return "Failure";
        }
    }
}