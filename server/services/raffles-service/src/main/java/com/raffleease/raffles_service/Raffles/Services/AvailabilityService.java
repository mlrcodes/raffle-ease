package com.raffleease.raffles_service.Raffles.Services;

import com.raffleease.common_models.DTO.Kafka.TicketsAvailability;
import com.raffleease.common_models.Exceptions.CustomExceptions.BusinessException;
import com.raffleease.raffles_service.Raffles.Model.Raffle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AvailabilityService {
    private final RafflesService rafflesService;

    public void modifyTicketsAvailability(TicketsAvailability request) {
        Raffle raffle = rafflesService.findById(request.raffleId());
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
        rafflesService.saveRaffle(raffle);
    }

    private void increaseAvailableTickets(Raffle raffle, long increaseQuantity) {
        Long availableTickets = raffle.getAvailableTickets() + increaseQuantity;
        if (availableTickets > raffle.getTotalTickets()) {
            throw new BusinessException("The operation exceeds the total ticket limit");
        }
        raffle.setAvailableTickets(availableTickets);
        rafflesService.saveRaffle(raffle);
    }

}
