package com.raffleease.raffles_service.Tickets.Services;

import com.raffleease.raffles_service.Exceptions.CustomExceptions.BusinessException;
import com.raffleease.raffles_service.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.raffles_service.Raffles.Model.Raffle;
import com.raffleease.raffles_service.Raffles.Services.RafflesService;
import com.raffleease.raffles_service.Tickets.DTO.GenerateRandomRequest;
import com.raffleease.raffles_service.Tickets.DTO.TicketResponse;
import com.raffleease.raffles_service.Tickets.Mappers.TicketsMapper;
import com.raffleease.raffles_service.Tickets.Models.Ticket;
import com.raffleease.raffles_service.Tickets.Models.TicketStatus;
import com.raffleease.raffles_service.Tickets.Repositories.TicketsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GenerateRandomService {
    private final ReservationService reservationService;
    private final RafflesService raffleService;
    private final TicketsRepository repository;
    private final TicketsMapper mapper;

    public Set<TicketResponse> generateRandom(GenerateRandomRequest request) {
        Raffle raffle = raffleService.findById(request.raffleId());
        Set<Ticket> availableTickets = findAvailableTickets(raffle);
        validateTicketAvailability(availableTickets, request.quantity());
        Set<Ticket> selectedTickets = selectRandomTickets(availableTickets, request.quantity());
        reserveTickets(raffle, selectedTickets);
        return mapper.fromTicketSetToTicketResponseSet(selectedTickets);
    }

    private Set<Ticket> findAvailableTickets(Raffle raffle) {
        try {
            return new HashSet<>(repository.findByRaffleAndStatus(raffle, TicketStatus.AVAILABLE));
        } catch (DataAccessException exp) {
            throw new DataBaseHandlingException("Error when retrieving tickets info");
        }
    }

    private void validateTicketAvailability(Set<Ticket> availableTickets, Long requestedQuantity) {
        if (availableTickets.isEmpty() || availableTickets.size() < requestedQuantity) {
            throw new BusinessException("Not enough tickets were found for this order");
        }
    }

    private void reserveTickets(Raffle raffle, Set<Ticket> tickets) {
        Set<Long> ticketIds = tickets.stream()
                .map(Ticket::getId)
                .collect(Collectors.toSet());
        reservationService.reserve(raffle, ticketIds);
    }

    private Set<Ticket> selectRandomTickets(Set<Ticket> availableTickets, Long quantity) {
        List<Ticket> tickets = new ArrayList<>(availableTickets);
        Collections.shuffle(tickets);
        tickets = tickets.subList(0, quantity.intValue());
        return new HashSet<>(tickets);
    }
}