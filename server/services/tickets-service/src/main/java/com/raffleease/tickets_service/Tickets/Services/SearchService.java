package com.raffleease.tickets_service.Tickets.Services;

import com.raffleease.common_models.DTO.Tickets.SearchRequest;
import com.raffleease.common_models.DTO.Tickets.TicketDTO;
import com.raffleease.common_models.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.common_models.Exceptions.CustomExceptions.ObjectNotFoundException;
import com.raffleease.tickets_service.Tickets.Mappers.TicketsMapper;
import com.raffleease.tickets_service.Tickets.Models.Ticket;
import com.raffleease.tickets_service.Tickets.Repositories.ITicketsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.raffleease.common_models.DTO.Tickets.TicketStatus.AVAILABLE;

@RequiredArgsConstructor
@Service
public class SearchService {
    private final TicketsService ticketsService;
    private final TicketsMapper mapper;
    private final ITicketsRepository ticketsRepository;

    public List<TicketDTO> findByTicketNumber(SearchRequest request) {
        Set<Ticket> searchResults = searchTicketsByNumber(request.raffleId(), request.ticketNumber());
        List<Ticket> sortedResult = sortTicketsByNumber(searchResults);
        return mapper.fromTicketList(sortedResult);
    }

    private Set<Ticket> searchTicketsByNumber(Long raffleId, String ticketNumber) {
        try {
            List<Ticket> searchResults = ticketsRepository.findByRaffleIdAndStatusAndTicketNumberContaining(
                    raffleId,
                    AVAILABLE,
                    ticketNumber
            );
            if (searchResults.isEmpty()) {
                throw new ObjectNotFoundException("No ticket for search was found");
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

    public String getHighestTicketNumber(Long raffleId) {
        Ticket ticket = ticketsRepository.findTopByRaffleIdOrderByTicketNumberDesc(raffleId)
                .orElseThrow(() -> new ObjectNotFoundException("No tickets found for the given raffle"));
        return ticket.getTicketNumber();
    }
}