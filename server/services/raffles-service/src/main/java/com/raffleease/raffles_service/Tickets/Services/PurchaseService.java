package com.raffleease.raffles_service.Tickets.Services;

import com.raffleease.raffles_service.Tickets.DTO.PurchaseRequest;
import com.raffleease.raffles_service.Tickets.DTO.TicketResponse;
import com.raffleease.raffles_service.Tickets.Mappers.TicketsMapper;
import com.raffleease.raffles_service.Tickets.Models.Ticket;
import com.raffleease.raffles_service.Tickets.Models.TicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PurchaseService {

    private final TicketsService ticketsService;
    private final TicketsMapper mapper;

    public Set<TicketResponse> purchase(PurchaseRequest request) {
        List<Ticket> tickets = ticketsService.findAllById(request.ticketsIds());
        Set<Ticket> ticketsToSell = tickets.stream().peek(ticket -> {
            ticket.setStatus(TicketStatus.SOLD);
            ticket.setCustomerId(request.customerId());
        }).collect(Collectors.toSet());
        Set<Ticket> savedTickets = new HashSet<>(ticketsService.saveAll(ticketsToSell));
        return mapper.fromTicketSetToTicketResponseSet(savedTickets);
    }
}
