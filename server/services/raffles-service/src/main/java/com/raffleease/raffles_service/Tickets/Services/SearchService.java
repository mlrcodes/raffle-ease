package com.raffleease.raffles_service.Tickets.Services;

import com.raffleease.raffles_service.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.raffles_service.Exceptions.CustomExceptions.TicketNotFoundException;
import com.raffleease.raffles_service.Raffles.Model.Raffle;
import com.raffleease.raffles_service.Raffles.Services.RafflesService;
import com.raffleease.raffles_service.Tickets.DTO.SearchRequest;
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
public class SearchService {
    private final TicketsService ticketsService;
    private final TicketsMapper mapper;
    private final TicketsRepository repository;
    private final RafflesService raffleService;

    public Set<TicketResponse> findByTicketNumber(SearchRequest request) {
        Raffle raffle = raffleService.findById(request.raffleId());
        Set<Ticket> searchResults = searchTicketsByNumber(raffle, request.ticketNumber());
        List<Ticket> sortedResult = sortTicketsByNumber(searchResults);
        return mapper.fromTicketSetToTicketResponseSet(new LinkedHashSet<>(sortedResult));
    }

    private Set<Ticket> searchTicketsByNumber(Raffle raffle, String ticketNumber) {
        try {
            List<Ticket> searchResults = repository.findByRaffleAndStatusAndTicketNumberContaining(
                    raffle,
                    TicketStatus.AVAILABLE,
                    ticketNumber
            );
            if (searchResults.isEmpty()) {
                throw new TicketNotFoundException("No ticket for search was found");
            }
            return new HashSet<>(searchResults);
        } catch (DataAccessException ex) {
            throw new DataBaseHandlingException("Failed to access database when searching tickets");
        }
    }

    private List<Ticket> sortTicketsByNumber(Set<Ticket> tickets) {
        return tickets.stream()
                .sorted(Comparator.comparing(Ticket::getTicketNumber))
                        .collect(Collectors.toList());
    }
}