package com.raffleease.raffles_service.Raffles.DTO;

import com.raffleease.raffles_service.Raffles.Models.RaffleStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record RaffleResponse(

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