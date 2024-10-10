package com.raffleease.raffles_service.Tickets.Services;

import com.raffleease.raffles_service.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.raffles_service.Exceptions.CustomExceptions.TicketNotFoundException;
import com.raffleease.raffles_service.Raffles.Models.Raffle;
import com.raffleease.raffles_service.Raffles.Services.RafflesService;
import com.raffleease.raffles_service.Tickets.DTO.SearchRequest;
import com.raffleease.raffles_service.Tickets.DTO.TicketResponse;
import com.raffleease.raffles_service.Tickets.DTO.TicketResponseSet;
import com.raffleease.raffles_service.Tickets.Mappers.TicketsMapper;
import com.raffleease.raffles_service.Tickets.Models.Ticket;
import com.raffleease.raffles_service.Tickets.Models.TicketStatus;
import com.raffleease.raffles_service.Tickets.Repositories.TicketsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final TicketsService ticketsService;
    private final TicketsMapper mapper;
    private final TicketsRepository repository;
    private final RafflesService raffleService;

    public TicketResponseSet findByTicketNumber(SearchRequest request) {
        Raffle raffle = findRaffleById(request.raffleId());
        List<Ticket> searchResults = searchTicketsByNumber(raffle, request.ticketNumber());
        List<Ticket> sortedResult = sortTicketsByNumber(searchResults);
        Set<TicketResponse> mappedTickets = mapTicketsToResponse(sortedResult);
        return buildTicketResponse(mappedTickets);
    }

    private Raffle findRaffleById(Long raffleId) {
        return raffleService.findById(raffleId);
    }

    private List<Ticket> searchTicketsByNumber(Raffle raffle, String ticketNumber) {
        try {
            List<Ticket> searchResults = repository.findByRaffleAndStatusAndTicketNumberContaining(
                    raffle,
                    TicketStatus.AVAILABLE,
                    ticketNumber
            );
            if (searchResults.isEmpty()) {
                throw new TicketNotFoundException("No tickets for search were found");
            }
            return searchResults;
        } catch (DataAccessException ex) {
            throw new DataBaseHandlingException("Failed to access database when searching tickets");
        }
    }

    private List<Ticket> sortTicketsByNumber(List<Ticket> tickets) {
        return tickets.stream()
                .sorted(Comparator.comparing(Ticket::getTicketNumber))
                .collect(Collectors.toList());
    }

    private Set<TicketResponse> mapTicketsToResponse(List<Ticket> sortedTickets) {
        return mapper.fromTicketSetToTicketResponseSet(new HashSet<>(sortedTickets));
    }

    private TicketResponseSet buildTicketResponse(Set<TicketResponse> mappedTickets) {
        return ticketsService.buildResponseFromSet(mappedTickets);
    }
}