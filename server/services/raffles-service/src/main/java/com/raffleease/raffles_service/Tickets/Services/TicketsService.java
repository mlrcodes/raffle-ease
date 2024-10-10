package com.raffleease.raffles_service.Tickets.Services;

import com.raffleease.raffles_service.Exceptions.CustomExceptions.BusinessException;
import com.raffleease.raffles_service.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.raffles_service.Exceptions.CustomExceptions.TicketNotFoundException;
import com.raffleease.raffles_service.Raffles.Models.Raffle;
import com.raffleease.raffles_service.Tickets.DTO.*;
import com.raffleease.raffles_service.Tickets.Mappers.TicketsMapper;
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

    private final TicketsMapper mapper;
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

    public Set<TicketResponse> purchase(PurchaseRequest request) {
        Set<TicketResponse> purchasedTickets = new HashSet<>();
        List<Ticket> tickets = findAllById(request.ticketsIds());
        tickets.forEach(ticket -> {
            ticket.setStatus(TicketStatus.SOLD);
            ticket.setCustomerId(request.customerId());
            Ticket savedTicket = repository.save(ticket);
            purchasedTickets.add(mapper.fromTicketToTicketResponse(savedTicket));
        });
        return purchasedTickets;
    }

    public void reserve(TicketsIdsDTO request) {
        List<Ticket> tickets = findAllById(request.tickets());
        checkTicketsAvailability(tickets);
        updateStatus(tickets, TicketStatus.RESERVED);
    }

    public void release(TicketsIdsDTO request) {
        List<Ticket> tickets = findAllById(request.tickets());
        updateStatus(tickets, TicketStatus.AVAILABLE);
    }

    private void updateStatus(List<Ticket> tickets, TicketStatus status) {
        tickets.forEach(ticket -> ticket.setStatus(status));
        try {
            this.repository.saveAll(tickets);
        } catch (DataAccessException exp) {
            throw new DataBaseHandlingException("Failed to update tickets status: " + exp.getMessage());
        }
    }

    public void checkTicketsAvailability(List<Ticket> tickets) {
        tickets.forEach(ticket -> {
            if (ticket.getStatus() != TicketStatus.AVAILABLE) {
                throw new BusinessException("One or more tickets are not available");
            }
        });
    }

    public TicketResponseSet buildResponseFromSet(Set<TicketResponse> ticketResponses) {
        return TicketResponseSet.builder()
                .tickets(ticketResponses)
                .build();
    }

    public Boolean checkIfReserved(TicketsIdsDTO request) {
        List<Ticket> tickets = findAllById(request.tickets());
        return tickets.stream().anyMatch(ticket -> !ticket.getStatus().equals(TicketStatus.RESERVED));
    }

    public List<Ticket> findAllById(Set<Long> ticketsIds) {
        List<Ticket> tickets = repository.findAllById(ticketsIds);
        if (tickets.isEmpty() || (ticketsIds.size() != tickets.size())) {
            throw new TicketNotFoundException("One ore more tickets were not found");
        }
        return tickets;
    }
}