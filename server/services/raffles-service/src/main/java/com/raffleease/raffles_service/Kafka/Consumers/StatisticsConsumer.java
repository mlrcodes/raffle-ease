package com.raffleease.raffles_service.Kafka.Consumers;

import com.raffleease.common_models.DTO.Kafka.PurchaseStatistics;
import com.raffleease.raffles_service.Raffles.Services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StatisticsConsumer {
    private final StatisticsService service;

    @KafkaListener(topics = "purchase-statistics-topic", groupId = "raffles-group")
    public void consumeStatistics (
            PurchaseStatistics statistics
    ) {
        service.updateStatistics(statistics);
    }
}
