package com.raffleease.payments_service.Kafka.Brokers.Producers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FailureProducer {
    /*
    private final KafkaTemplate<String, PFailureMessage> failureTemplate;


    public void notifyFailure(PFailureMessage request) {
        Message<PFailureMessage> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "failure-topic")
                .build();

        try {
            failureTemplate.send(message);
        } catch (KafkaException exp) {
            throw new CustomKafkaException("Error sending order failure notification: " + exp.getMessage());
        }
    }

     */
}
