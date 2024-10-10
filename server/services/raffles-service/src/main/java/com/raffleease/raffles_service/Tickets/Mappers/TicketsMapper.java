package com.raffleease.raffles_service.Tickets.Mappers;

import com.raffleease.raffles_service.Tickets.DTO.TicketResponse;
import com.raffleease.raffles_service.Tickets.Models.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TicketsMapper {

    public TicketResponse fromTicketToTicketResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .raffleId(ticket.getRaffle().getId())
                .ticketNumber(ticket.getTicketNumber())
                .status(ticket.getStatus())
                .customerId(ticket.getCustomerId())
                .build();
    }

    public Set<TicketResponse> fromTicketSetToTicketResponseSet(Set<Ticket> tickets) {
        return tickets.stream()
                .map(this::fromTicketToTicketResponse)
                .collect(Collectors.toSet());
    }
}