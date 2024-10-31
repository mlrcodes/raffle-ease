package com.raffleease.raffles_service.Raffles.Mappers;

import com.raffleease.common_models.DTO.Raffles.RaffleCreationRequest;
import com.raffleease.common_models.DTO.Raffles.RaffleDTO;
import com.raffleease.raffles_service.Raffles.Model.Raffle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RafflesMapper {
    public Raffle toRaffle(RaffleCreationRequest request) {
        return Raffle.builder()
                .title(request.title())
                .description(request.description())
                .endDate(request.endDate())
                .ticketPrice(request.ticketsInfo().price())
                .availableTickets(request.ticketsInfo().amount())
                .totalTickets(request.ticketsInfo().amount())
                .photosURLs(request.photosURLs())
                .associationId(request.associationId())
                .build();
    }

    public RaffleDTO fromRaffle(Raffle raffle) {
        return RaffleDTO.builder()
                .id(raffle.getId())
                .title(raffle.getTitle())
                .description(raffle.getDescription())
                .startDate(raffle.getStartDate())
                .endDate(raffle.getEndDate())
                .status(raffle.getStatus())
                .photosURLs(raffle.getPhotosURLs())
                .ticketPrice(raffle.getTicketPrice())
                .availableTickets(raffle.getAvailableTickets())
                .totalTickets(raffle.getTotalTickets())
                .associationId(raffle.getAssociationId())
                .build();
    }

    public Set<RaffleDTO> fromRaffleSet(Set<Raffle> raffles) {
        return raffles.stream().map(this::fromRaffle).collect(Collectors.toSet());
    }
}