package com.raffleease.tickets_service.Tickets.Services;

import com.raffleease.common_models.DTO.Tickets.PurchaseRequest;
import com.raffleease.common_models.DTO.Tickets.TicketDTO;
import com.raffleease.tickets_service.Tickets.Mappers.TicketsMapper;
import com.raffleease.tickets_service.Tickets.Models.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.raffleease.common_models.DTO.Tickets.TicketStatus.SOLD;

@RequiredArgsConstructor
@Service
public class PurchaseService {

    private final TicketsService ticketsService;
    private final TicketsMapper mapper;

    public Set<TicketDTO> purchase(PurchaseRequest request) {
        List<Ticket> tickets = ticketsService.findAllById(request.ticketsIds());
        Set<Ticket> ticketsToSell = tickets.stream().peek(ticket -> {
            ticket.setStatus(SOLD);
            ticket.setReservationFlag(null);
            ticket.setReservationTime(null);
            ticket.setCustomerId(request.customerId());
        }).collect(Collectors.toSet());
        Set<Ticket> savedTickets = ticketsService.saveAll(ticketsToSell);
        return mapper.fromTicketSet(savedTickets);
    }
}