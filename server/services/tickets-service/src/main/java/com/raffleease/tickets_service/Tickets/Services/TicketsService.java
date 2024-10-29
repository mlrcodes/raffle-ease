package com.raffleease.tickets_service.Tickets.Services;

import com.raffleease.common_models.DTO.Tickets.RaffleTicketsCreationRequest;
import com.raffleease.common_models.DTO.Tickets.TicketStatus;
import com.raffleease.common_models.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.tickets_service.Tickets.Models.Ticket;
import com.raffleease.tickets_service.Tickets.Repositories.ITicketsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.raffleease.common_models.DTO.Tickets.TicketStatus.AVAILABLE;

@RequiredArgsConstructor
@Service
public class TicketsService {
    private final ITicketsRepository repository;

    public Set<String> createTickets(RaffleTicketsCreationRequest request) {
        Set<Ticket> tickets = LongStream.rangeClosed(request.lowerLimit(), request.upperLimit())
                .mapToObj(i -> Ticket.builder()
                        .raffleId(request.raffleId())
                        .status(AVAILABLE)
                        .ticketNumber(Long.toString(i))
                        .build()
                ).collect(Collectors.toSet());
        return saveAll(tickets).stream().map(Ticket::getId).collect(Collectors.toSet());
    }

    public List<Ticket> findAllById(Set<String> ticketsIds) {
        try {
            return repository.findAllById(ticketsIds);
        } catch (DataAccessException exp) {
            throw new DataBaseHandlingException("An error when retrieving tickets occurred");
        }
    }

    public Set<Ticket> saveAll(Set<Ticket> tickets) {
        try {
            return new HashSet<>(repository.saveAll(tickets));
        } catch (DataAccessException exp) {
            throw new DataBaseHandlingException("An error occurred when saving tickets occurred");
        }
    }
}