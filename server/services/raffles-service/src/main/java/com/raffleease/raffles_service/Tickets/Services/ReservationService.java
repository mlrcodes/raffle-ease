package com.raffleease.raffles_service.Tickets.Services;

import com.raffleease.raffles_service.Exceptions.CustomExceptions.BusinessException;
import com.raffleease.raffles_service.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.raffles_service.Tickets.DTO.TicketsIdsDTO;
import com.raffleease.raffles_service.Tickets.Models.Ticket;
import com.raffleease.raffles_service.Tickets.Models.TicketStatus;
import com.raffleease.raffles_service.Tickets.Repositories.TicketsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final TicketsService ticketsService;
    private final TicketsRepository repository;

    public void reserve(Set<Long> ticketsIds) {
        List<Ticket> tickets = ticketsService.findAllById(ticketsIds);
        checkTicketsAvailability(tickets);
        updateStatus(tickets, TicketStatus.RESERVED);
    }

    public void release(TicketsIdsDTO request) {
        List<Ticket> tickets = ticketsService.findAllById(request.tickets());
        updateStatus(tickets, TicketStatus.AVAILABLE);
    }

    public void checkTicketsAvailability(List<Ticket> tickets) {
        tickets.forEach(ticket -> {
            if (ticket.getStatus() != TicketStatus.AVAILABLE) {
                throw new BusinessException("One or more tickets are not available");
            }
        });
    }

    private void updateStatus(List<Ticket> tickets, TicketStatus status) {
        tickets.forEach(ticket -> ticket.setStatus(status));
        try {
            this.repository.saveAll(tickets);
        } catch (DataAccessException exp) {
            throw new DataBaseHandlingException("Failed to update tickets status: " + exp.getMessage());
        }
    }
}
