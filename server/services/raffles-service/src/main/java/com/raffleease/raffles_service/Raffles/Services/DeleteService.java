package com.raffleease.raffles_service.Raffles.Services;

import com.raffleease.common_models.DTO.Kafka.TicketsDelete;
import com.raffleease.common_models.Exceptions.CustomExceptions.BusinessException;
import com.raffleease.common_models.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.raffles_service.Kafka.Producers.TicketsDeleteProducer;
import com.raffleease.raffles_service.Raffles.Model.Raffle;
import com.raffleease.raffles_service.Raffles.Model.RaffleImage;
import com.raffleease.raffles_service.Raffles.Repositories.RafflesRepository;
import com.raffleease.raffles_service.S3.Services.S3Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.raffleease.common_models.DTO.Raffles.RaffleStatus.PENDING;

@RequiredArgsConstructor
@Service
public class DeleteService {
    private final TicketsDeleteProducer ticketsDeleteProducer;
    private final RafflesService rafflesService;
    private final S3Service s3Service;
    private final RafflesRepository repository;

    @Transactional
    public void delete(Long id) {
        Raffle raffle = rafflesService.findById(id);
        checkIfPending(raffle);
        List<String> images = raffle.getImages().stream().map(RaffleImage::getKey).collect(Collectors.toList());
        deleteDBRegistry(id);
        ticketsDeleteProducer.deleteTickets(
                TicketsDelete.builder()
                        .raffleId(id)
                        .build()
        );
        CompletableFuture.runAsync(() -> s3Service.delete(images));
    }

    private void checkIfPending(Raffle raffle) {
        if (!raffle.getStatus().equals(PENDING)) {
            throw new BusinessException("Cannot delete already published raffle");
        }
    }

    private void deleteDBRegistry(Long id) {
        try {
            repository.deleteById(id);
        } catch (DataAccessException exp) {
            throw new DataBaseHandlingException("Failed to access database when deleting raffle");
        }
    }
}