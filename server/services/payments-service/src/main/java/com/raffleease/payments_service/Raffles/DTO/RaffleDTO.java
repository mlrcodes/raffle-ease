package com.raffleease.payments_service.Raffles.DTO;

import com.raffleease.payments_service.Raffles.Model.RaffleStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record RaffleDTO(

        Long id,

        String title,

        String description,

        LocalDateTime startDate,

        LocalDateTime endDate,

        RaffleStatus status,

        Set<String> photosURLs,

        BigDecimal ticketPrice,

        Long availableTickets,

        Long associationId
) {
}