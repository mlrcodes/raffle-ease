package com.raffleease.raffles_service.Raffles.Services;

import com.raffleease.common_models.DTO.Raffles.EditRaffle;
import com.raffleease.common_models.DTO.Raffles.RaffleDTO;
import com.raffleease.common_models.DTO.Tickets.RaffleTicketsCreationRequest;
import com.raffleease.common_models.Exceptions.CustomExceptions.BusinessException;
import com.raffleease.raffles_service.Raffles.Mappers.RafflesMapper;
import com.raffleease.raffles_service.Raffles.Model.Raffle;
import com.raffleease.raffles_service.Raffles.Model.RaffleImage;
import com.raffleease.raffles_service.S3.Services.S3Service;
import com.raffleease.raffles_service.Tickets.TicketsClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EditService {
    private final RafflesService rafflesService;
    private final RafflesMapper mapper;
    private final S3Service s3Service;
    private final TicketsClient ticketsClient;

    @Transactional
    public RaffleDTO edit(Long id, EditRaffle editRaffle) {
        Raffle raffle = rafflesService.findById(id);

        if (!Objects.isNull(editRaffle.title())) {
            raffle.setTitle(editRaffle.title());
        }

        if (editRaffle.description() != null) {
            raffle.setDescription(editRaffle.description());
        }

        if (editRaffle.endDate() != null) {
            raffle.setEndDate(editRaffle.endDate());
        }

        if (editRaffle.imageKeys() != null) {
            editImages(raffle, editRaffle.imageKeys());
        }

        if (editRaffle.ticketPrice() != null) {
            raffle.setTicketPrice(editRaffle.ticketPrice());
        }

        if (editRaffle.totalTickets() != null) {
            editTotalTickets(raffle, editRaffle.totalTickets());
        }

        Raffle savedRaffle = rafflesService.saveRaffle(raffle);
        return mapper.fromRaffle(savedRaffle);
    }

    private void editImages(Raffle raffle, List<String> newKeys) {
        List<RaffleImage> oldImages = raffle.getImages();

        List<String> oldKeys = oldImages.stream()
                .map(RaffleImage::getKey)
                .toList();

        List<String> deleteKeys = oldKeys.stream()
                .filter(k -> !newKeys.contains(k))
                .collect(Collectors.toList());

        if (!deleteKeys.isEmpty()) {
            raffle.getImages().removeIf(image -> deleteKeys.contains(image.getKey()));

            s3Service.delete(deleteKeys);
        }

        List<String> addKeys = new ArrayList<>(newKeys);
        addKeys.removeAll(oldKeys);

        for (String newKey : addKeys) {
            RaffleImage newImage = RaffleImage.builder()
                    .key(newKey)
                    .raffle(raffle)
                    .build();
            raffle.getImages().add(newImage);
        }
    }

    private void editTotalTickets(Raffle raffle, long editTotal) {
        if (raffle.getSoldTickets() != null && editTotal < raffle.getSoldTickets()) {
            throw new BusinessException("The total tickets count cannot be less than the number of tickets already sold for this raffle");
        }

        long oldTotal = raffle.getTotalTickets();
        raffle.setTotalTickets(editTotal);

        long ticketDifference = editTotal - oldTotal;
        raffle.setAvailableTickets(raffle.getAvailableTickets() + ticketDifference);

        if (ticketDifference > 0) {
            createAdditionalTickets(raffle, ticketDifference);
        }
    }

    private void createAdditionalTickets(Raffle raffle, long ticketDifference) {
        String highestTicketNumber = ticketsClient.getHighestTicketNumber(raffle.getId());
        long lowerLimit = Long.parseLong(highestTicketNumber);

        RaffleTicketsCreationRequest request = new RaffleTicketsCreationRequest(
                ticketDifference,
                raffle.getTicketPrice(),
                lowerLimit
        );

        Set<String> newTickets = ticketsClient.createTickets(request);
        raffle.getTickets().addAll(newTickets);
    }
}
