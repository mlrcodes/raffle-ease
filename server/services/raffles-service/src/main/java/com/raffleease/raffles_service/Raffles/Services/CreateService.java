package com.raffleease.raffles_service.Raffles.Services;

import com.raffleease.common_models.DTO.Kafka.TicketsRaffle;
import com.raffleease.common_models.DTO.Raffles.CreateRaffle;
import com.raffleease.common_models.DTO.Raffles.RaffleDTO;
import com.raffleease.common_models.DTO.Tickets.RaffleTicketsCreationRequest;
import com.raffleease.common_models.Exceptions.CustomExceptions.ObjectNotFoundException;
import com.raffleease.common_models.Exceptions.CustomExceptions.TicketsCreationException;
import com.raffleease.raffles_service.Associations.AssociationsClient;
import com.raffleease.raffles_service.Auth.AuthClient;
import com.raffleease.raffles_service.Kafka.Producers.TicketsRaffleProducer;
import com.raffleease.raffles_service.Raffles.Mappers.ImagesMapper;
import com.raffleease.raffles_service.Raffles.Mappers.RafflesMapper;
import com.raffleease.raffles_service.Raffles.Model.Raffle;
import com.raffleease.raffles_service.Raffles.Model.RaffleImage;
import com.raffleease.raffles_service.Tickets.TicketsClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.raffleease.common_models.DTO.Raffles.RaffleStatus.PENDING;

@Service
@RequiredArgsConstructor
public class CreateService {
    private final RafflesService rafflesService;
    private final RafflesMapper rafflesMapper;
    private final ImagesMapper imagesMapper;
    private final TicketsClient ticketsClient;
    private final AssociationsClient associationsClient;
    private final TicketsRaffleProducer ticketsRaffleProducer;
    private final AuthClient authClient;

    @Transactional
    public RaffleDTO createRaffle(CreateRaffle request, String authHeader) {
        Long associationId = authClient.getId(authHeader);
        validateAssociationExists(associationId);
        Raffle raffle = rafflesMapper.toRaffle(request);
        raffle.setAssociationId(associationId);
        Set<String> createdTickets = createTickets(request.ticketsInfo());
        raffle.setTickets(createdTickets);
        raffle.setStatus(PENDING);
        Raffle savedRaffle = rafflesService.saveRaffle(raffle);
        setRaffleImages(request.imageKeys(), savedRaffle);
        Raffle updatedRaffle = rafflesService.saveRaffle(savedRaffle);
        setTicketsRaffle(raffle.getId(), createdTickets);
        return rafflesMapper.fromRaffle(updatedRaffle);
    }

    private void validateAssociationExists(Long associationId) {
        if (!associationsClient.existsById(associationId)) {
            throw new ObjectNotFoundException("Association not found");
        }
    }

    private Set<String> createTickets(RaffleTicketsCreationRequest request) {
        try {
            return ticketsClient.createTickets(request);
        } catch (RuntimeException exp) {
            throw new TicketsCreationException("Error creating tickets");
        }
    }

    private void setTicketsRaffle(Long raffleId, Set<String> tickets) {
        ticketsRaffleProducer.produceTicketsRaffle(
                TicketsRaffle.builder()
                        .raffleId(raffleId)
                        .tickets(tickets)
                        .build()
        );
    }

    private void setRaffleImages(List<String> imageKeys, Raffle raffle) {
        if (imageKeys == null) return;
        List<RaffleImage> images = imagesMapper.toImages(imageKeys, raffle);
        List<RaffleImage> savedImages = rafflesService.saveImages(images);
        raffle.setImages(savedImages);
    }
}