package com.raffleease.raffles_service.Tickets.Services;

import com.raffleease.raffles_service.Exceptions.CustomExceptions.BusinessException;
import com.raffleease.raffles_service.Raffles.Models.Raffle;
import com.raffleease.raffles_service.Raffles.Services.RafflesService;
import com.raffleease.raffles_service.Tickets.DTO.GenerateRandomRequest;
import com.raffleease.raffles_service.Tickets.DTO.TicketResponse;
import com.raffleease.raffles_service.Tickets.DTO.TicketResponseSet;
import com.raffleease.raffles_service.Tickets.Mappers.TicketsMapper;
import com.raffleease.raffles_service.Tickets.Models.Ticket;
import com.raffleease.raffles_service.Tickets.Models.TicketStatus;
import com.raffleease.raffles_service.Tickets.Repositories.TicketsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class GenerateRandomService {

    private final TicketsService ticketsService;

    private final RafflesService raffleService;

    private final TicketsMapper mapper;

    private final TicketsRepository repository;

    public TicketResponseSet generateRandom(GenerateRandomRequest request) {
        Raffle raffle = findRaffleById(request.raffleId());
        List<Ticket> availableTickets = findAvailableTickets(raffle);
        validateTicketAvailability(availableTickets, request.quantity());
        List<Ticket> selectedTickets = selectRandomTickets(availableTickets, request.quantity());
        Set<TicketResponse> ticketResponses = mapTicketsToResponses(selectedTickets);
        return ticketsService.buildResponseFromSet(ticketResponses);
    }

    private Raffle findRaffleById(Long raffleId) {
        return raffleService.findById(raffleId);
    }

    private List<Ticket> findAvailableTickets(Raffle raffle) {
        return repository.findByRaffleAndStatus(raffle, TicketStatus.AVAILABLE);
    }

    private void validateTicketAvailability(List<Ticket> availableTickets, Long requestedQuantity) {
        if (availableTickets.isEmpty() || availableTickets.size() < requestedQuantity) {
            throw new BusinessException("No tickets were found for this order");
        }
    }

    private List<Ticket> selectRandomTickets(List<Ticket> availableTickets, Long quantity) {
        Collections.shuffle(availableTickets);
        return availableTickets.subList(0, quantity.intValue());
    }

    private Set<TicketResponse> mapTicketsToResponses(List<Ticket> selectedTickets) {
        return this.mapper.fromTicketSetToTicketResponseSet(new HashSet<>(selectedTickets));
    }
}