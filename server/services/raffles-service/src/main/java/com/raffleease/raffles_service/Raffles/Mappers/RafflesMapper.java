package com.raffleease.raffles_service.Raffles.Mappers;

import com.raffleease.common_models.DTO.Raffles.CreateRaffle;
import com.raffleease.common_models.DTO.Raffles.RaffleDTO;
import com.raffleease.raffles_service.Raffles.Model.Raffle;
import com.raffleease.raffles_service.Raffles.Model.RaffleImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RafflesMapper {
    private final ImagesMapper imagesMapper;

    public Raffle toRaffle(CreateRaffle request) {
        return Raffle.builder()
                .title(request.title())
                .description(request.description())
                .endDate(request.endDate())
                .ticketPrice(request.ticketsInfo().price())
                .availableTickets(request.ticketsInfo().amount())
                .totalTickets(request.ticketsInfo().amount())
                .firstTicketNumber(request.ticketsInfo().lowerLimit())
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
                .imageKeys(imagesMapper.fromImages(raffle.getImages()))
                .ticketPrice(raffle.getTicketPrice())
                .firstTicketNumber(raffle.getFirstTicketNumber())
                .availableTickets(raffle.getAvailableTickets())
                .totalTickets(raffle.getTotalTickets())
                .soldTickets(raffle.getSoldTickets())
                .revenue(raffle.getRevenue())
                .associationId(raffle.getAssociationId())
                .build();
    }

    public Set<RaffleDTO> fromRaffleSet(Set<Raffle> raffles) {
        return raffles.stream().map(this::fromRaffle).collect(Collectors.toSet());
    }
}