package com.raffleease.raffles_service.Raffles.Services;

import com.raffleease.common_models.DTO.Kafka.TicketsAvailabilityRequest;
import com.raffleease.common_models.DTO.Raffles.RaffleDTO;
import com.raffleease.common_models.DTO.Raffles.RaffleStatus;
import com.raffleease.raffles_service.Exceptions.CustomExceptions.BusinessException;
import com.raffleease.raffles_service.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.raffles_service.Exceptions.CustomExceptions.RafflesNotFoundException;
import com.raffleease.raffles_service.Raffles.Mappers.RafflesMapper;
import com.raffleease.raffles_service.Raffles.Model.Raffle;
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

    public RaffleDTO get(Long id) {
        Raffle raffle = findById(id);
        return mapper.fromRaffle(raffle);
    }

    public Long publish(Long id) {
        Raffle raffle = findById(id);
        raffle.setStatus(RaffleStatus.ACTIVE);
        raffle.setStartDate(LocalDateTime.now());
        return saveRaffle(raffle).getId();
    }

    public void modifyTicketsAvailability(TicketsAvailabilityRequest request) {
        Raffle raffle = findById(request.raffleId());
        switch (request.operationType()) {
            case DECREASE -> reduceAvailableTickets(raffle, request.quantity());
            case INCREASE -> increaseAvailableTickets(raffle, request.quantity());
        }
    }

    private void reduceAvailableTickets(Raffle raffle, long reductionQuantity) {
        long availableTickets = raffle.getAvailableTickets() - reductionQuantity;
        if (availableTickets < 0) {
            throw new BusinessException("Insufficient tickets available to complete the operation");
        }
        raffle.setAvailableTickets(availableTickets);
        saveRaffle(raffle);
    }

    private void increaseAvailableTickets(Raffle raffle, long increaseQuantity) {
        Long availableTickets = raffle.getAvailableTickets() + increaseQuantity;
        if (availableTickets > raffle.getTotalTickets()) {
            throw new BusinessException("The operation exceeds the total ticket limit");
        }
        raffle.setAvailableTickets(availableTickets);
        saveRaffle(raffle);
    }

    public Raffle findById(Long id) {
        try {
            return this.repository.findById(id)
                    .orElseThrow(() -> new RafflesNotFoundException("Raffle not found"));
        } catch (DataAccessException exp) {
            throw new DataBaseHandlingException("Failed to access database when retrieving raffle's information");
        }
    }

    public Raffle saveRaffle(Raffle raffle) {
        try {
            return repository.save(raffle);
        } catch (DataAccessException exp) {
            throw new DataBaseHandlingException("Failed to access database when saving raffle");
        }
    }
}
