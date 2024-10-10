package com.raffleease.raffles_service.Raffles.Services;

import com.raffleease.raffles_service.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.raffles_service.Exceptions.CustomExceptions.RafflesNotFoundException;
import com.raffleease.raffles_service.Raffles.DTO.RaffleResponse;
import com.raffleease.raffles_service.Raffles.Mappers.RafflesMapper;
import com.raffleease.raffles_service.Raffles.Models.Raffle;
import com.raffleease.raffles_service.Raffles.Models.RaffleStatus;
import com.raffleease.raffles_service.Raffles.Repositories.RafflesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class RafflesService {

    private final RafflesRepository repository;

    private final RafflesMapper mapper;

    public RaffleResponse get(Long id) {
        return mapRaffle(findById(id));
    }

    public Long publish(Long id) {
        Raffle raffle = findById(id);
        raffle.setStatus(RaffleStatus.ACTIVE);
        raffle.setStartDate(LocalDateTime.now());
        return repository.save(raffle).getId();
    }

    public Raffle findById(Long id) {
        try {
            return this.repository.findById(id)
                    .orElseThrow(() -> new RafflesNotFoundException("Raffle not found"));
        } catch (DataAccessException exp) {
            throw new DataBaseHandlingException("Failed to access database when retrieving raffle's information");
        }
    }

    private RaffleResponse mapRaffle(Raffle raffle) {
        return mapper.fromRaffleToRaffleResponse(raffle);
    }
}
