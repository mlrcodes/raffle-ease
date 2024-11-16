package com.raffleease.raffles_service.Raffles.Services;

import com.raffleease.common_models.DTO.Kafka.PurchaseStatistics;
import com.raffleease.raffles_service.Raffles.Model.Raffle;
import com.raffleease.raffles_service.Raffles.Repositories.RafflesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StatisticsService {
    private final RafflesService rafflesService;

    public void updateStatistics(PurchaseStatistics statistics) {
        Raffle raffle = rafflesService.findById(statistics.raffleId());
        raffle.setSoldTickets(raffle.getSoldTickets() + statistics.quantity());
        raffle.setRevenue(
                raffle.getRevenue().add(
                        Optional.ofNullable(statistics.total()).orElse(BigDecimal.ZERO)
                )
        );
        rafflesService.saveRaffle(raffle);
    }
}
