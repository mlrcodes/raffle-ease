package com.raffleease.raffles_service.Raffles.Mappers;

import com.raffleease.raffles_service.Raffles.DTO.RaffleCreationRequest;
import com.raffleease.raffles_service.Raffles.DTO.RaffleResponse;
import com.raffleease.raffles_service.Raffles.Models.Raffle;
import com.raffleease.raffles_service.Tickets.Mappers.TicketsMapper;
import com.raffleease.raffles_service.Tickets.Models.Ticket;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class RafflesMapper {

    private final TicketsMapper ticketsMapper;

    public Raffle toRaffle(RaffleCreationRequest request) {
        return Raffle.builder()
                .title(request.title())
                .description(request.description())
                .endDate(request.endDate())
                .ticketPrice(request.ticketsInfo().price())
                .availableTickets(request.ticketsInfo().amount())
                .photosURLs(request.photosURLs())
                .associationId(request.associationId())
                .build();
    }

    @OneToMany(mappedBy = "raffle", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ticket> tickets;

    @Column(nullable = false)
    private Long associationId;

    public RaffleResponse fromRaffleToRaffleResponse(Raffle raffle) {
        return RaffleResponse.builder()
                .id(raffle.getId())
                .title(raffle.getTitle())
                .description(raffle.getDescription())
                .startDate(raffle.getStartDate())
                .endDate(raffle.getEndDate())
                .status(raffle.getStatus())
                .photosURLs(raffle.getPhotosURLs())
                .ticketPrice(raffle.getTicketPrice())
                .availableTickets(raffle.getAvailableTickets())
                .associationId(raffle.getAssociationId())
                .build();
    }
}