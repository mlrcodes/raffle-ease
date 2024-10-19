package com.raffleease.raffles_service.Tickets.Services;

import com.raffleease.raffles_service.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.raffles_service.Raffles.Model.Raffle;
import com.raffleease.raffles_service.Tickets.DTO.*;
import com.raffleease.raffles_service.Tickets.Models.Ticket;
import com.raffleease.raffles_service.Tickets.Models.TicketStatus;
import com.raffleease.raffles_service.Tickets.Repositories.TicketsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@RequiredArgsConstructor
@Service
public class TicketsService {
    private final TicketsRepository repository;

    public Set<Ticket> createTickets(RaffleTicketsCreationRequest ticketsInfo, Raffle raffle) {
        return LongStream.rangeClosed(ticketsInfo.lowerLimit(), ticketsInfo.upperLimit())
                .mapToObj(i -> Ticket.builder()
                        .raffle(raffle)
                        .status(TicketStatus.AVAILABLE)
                        .ticketNumber(Long.toString(i))
                        .build()
                ).collect(Collectors.toSet());
    }

    public List<Ticket> findAllById(Set<Long> ticketsIds) {
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