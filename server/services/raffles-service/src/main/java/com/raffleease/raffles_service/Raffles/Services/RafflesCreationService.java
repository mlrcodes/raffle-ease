package com.raffleease.raffles_service.Raffles.Services;

import com.raffleease.raffles_service.Associations.AssociationsClient;
import com.raffleease.raffles_service.Exceptions.CustomExceptions.AssociationRetrievalException;
import com.raffleease.raffles_service.Raffles.DTO.RaffleCreationRequest;
import com.raffleease.raffles_service.Raffles.Mappers.RafflesMapper;
import com.raffleease.raffles_service.Raffles.Models.Raffle;
import com.raffleease.raffles_service.Raffles.Models.RaffleStatus;
import com.raffleease.raffles_service.Raffles.Repositories.RafflesRepository;
import com.raffleease.raffles_service.Tickets.Models.Ticket;
import com.raffleease.raffles_service.Tickets.Services.TicketsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RafflesCreationService {

    private final RafflesMapper mapper;

    private final TicketsService ticketsService;

    private final AssociationsClient associationsClient;

    private final RafflesRepository repository;

    @Transactional
    public Long createRaffle(RaffleCreationRequest request) {
        Raffle raffle = mapRaffle(request);
        validateAssociationExists(request.associationId());
        createAndSetTickets(raffle, request);
        setInitialStatus(raffle);
        return saveRaffleAndReturnId(raffle);
    }

    private Raffle mapRaffle(RaffleCreationRequest request) {
        return mapper.toRaffle(request);
    }

    private void validateAssociationExists(Long associationId) {
        if (!associationsClient.existsById(associationId)) {
            throw new AssociationRetrievalException("Association not found");
        }
    }

    private void createAndSetTickets(Raffle raffle, RaffleCreationRequest request) {
        Set<Ticket> tickets = ticketsService.createTickets(request.ticketsInfo(), raffle);
        raffle.setTickets(tickets);
    }

    private void setInitialStatus(Raffle raffle) {
        raffle.setStatus(RaffleStatus.PENDING);
    }

    private Long saveRaffleAndReturnId(Raffle raffle) {
        Raffle savedRaffle = repository.save(raffle);
        return savedRaffle.getId();
    }
}