package com.raffleease.raffles_service.Raffles.Services;

import com.raffleease.common_models.DTO.Raffles.RaffleCreationRequest;
import com.raffleease.common_models.DTO.Tickets.RaffleTicketsCreationRequest;
import com.raffleease.raffles_service.Associations.AssociationsClient;
import com.raffleease.raffles_service.Exceptions.CustomExceptions.AssociationRetrievalException;
import com.raffleease.raffles_service.Raffles.Mappers.RafflesMapper;
import com.raffleease.raffles_service.Raffles.Model.Raffle;
import com.raffleease.raffles_service.Raffles.Repositories.RafflesRepository;
import com.raffleease.raffles_service.Tickets.TicketsClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.raffleease.common_models.DTO.Raffles.RaffleStatus.PENDING;

@Service
@RequiredArgsConstructor
public class RafflesCreationService {

    private final RafflesService rafflesService;
    private final RafflesMapper mapper;
    private final TicketsClient ticketsClient;
    private final AssociationsClient associationsClient;
    private final RafflesRepository repository;

    @Transactional
    public Long createRaffle(RaffleCreationRequest request) {
        Raffle raffle = mapper.toRaffle(request);
        validateAssociationExists(request.associationId());
        Set<String> createdTicketsId = createTickets(request.ticketsInfo());
        raffle.setTicketsId(createdTicketsId);
        raffle.setStatus(PENDING);
        Raffle savedRaffle = rafflesService.saveRaffle(raffle);
        return savedRaffle.getId();
    }

    private void validateAssociationExists(Long associationId) {
        if (!associationsClient.existsById(associationId)) {
            throw new AssociationRetrievalException("Association not found");
        }
    }

    private Set<String> createTickets(RaffleTicketsCreationRequest request) {
        return ticketsClient.createTickets(request);
    }
}