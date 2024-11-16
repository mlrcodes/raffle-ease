package com.raffleease.raffles_service.Raffles.Services;

import com.raffleease.common_models.DTO.Raffles.RaffleDTO;
import com.raffleease.common_models.DTO.Raffles.RaffleStatus;
import com.raffleease.raffles_service.Raffles.Mappers.RafflesMapper;
import com.raffleease.raffles_service.Raffles.Model.Raffle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.raffleease.common_models.DTO.Raffles.RaffleStatus.ACTIVE;
import static com.raffleease.common_models.DTO.Raffles.RaffleStatus.PAUSED;

@RequiredArgsConstructor
@Service
public class StatusService {
    private final RafflesService rafflesService;
    private final RafflesMapper mapper;

    public RaffleDTO publish(Long id) {
        Raffle raffle = rafflesService.findById(id);
        raffle.setStartDate(LocalDateTime.now());
        return this.updateStatus(raffle, ACTIVE);
    }

    public RaffleDTO pause(Long id) {
        return this.updateStatus(id, PAUSED);
    }

    public RaffleDTO restart(Long id) {
        return this.updateStatus(id, ACTIVE);
    }

    private RaffleDTO updateStatus(Long id, RaffleStatus status) {
        Raffle raffle = rafflesService.findById(id);
        return this.updateStatus(raffle, status);
    }

    private RaffleDTO updateStatus(Raffle raffle, RaffleStatus status) {
        raffle.setStatus(status);
        Raffle savedRaffle = rafflesService.saveRaffle(raffle);
        return mapper.fromRaffle(savedRaffle);
    }
}
