package com.raffleease.tickets_service.Kafka.Consumer;

import com.raffleease.common_models.DTO.Kafka.TicketsRelease;
import com.raffleease.common_models.DTO.Tickets.ReservationRequest;
import com.raffleease.tickets_service.Tickets.Services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TicketsReleaseConsumer {
    private final ReservationService reservationService;

    @KafkaListener(topics = "tickets-release-topic", groupId = "tickets-group")
    public void consumeRelease (
            TicketsRelease request
    ) {
        reservationService.release(
                ReservationRequest.builder()
                        .raffleId(request.raffleId())
                        .ticketsIds(request.ticketsIds())
                        .build()
        );
    }
}
