package com.raffleease.raffles_service.Raffles.Services;

import com.raffleease.common_models.DTO.Raffles.RaffleDTO;
import com.raffleease.common_models.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.common_models.Exceptions.CustomExceptions.ObjectNotFoundException;
import com.raffleease.raffles_service.Auth.AuthClient;
import com.raffleease.raffles_service.Raffles.Mappers.RafflesMapper;
import com.raffleease.raffles_service.Raffles.Model.Raffle;
import com.raffleease.raffles_service.Raffles.Model.RaffleImage;
import com.raffleease.raffles_service.Raffles.Repositories.ImagesRepository;
import com.raffleease.raffles_service.Raffles.Repositories.RafflesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class RafflesService {
    private final RafflesRepository rafflesRepository;
    private final ImagesRepository imagesRepository;
    private final RafflesMapper mapper;
    private final AuthClient authClient;

    public RaffleDTO get(Long id) {
        Raffle raffle = findById(id);
        return mapper.fromRaffle(raffle);
    }

    public Raffle findById(Long id) {
        try {
            return this.rafflesRepository.findById(id)
                    .orElseThrow(() -> new ObjectNotFoundException("Raffle not found"));
        } catch (DataAccessException exp) {
            throw new DataBaseHandlingException("Failed to access database when retrieving raffle's information");
        }
    }

    public Raffle saveRaffle(Raffle raffle) {
        try {
            return rafflesRepository.save(raffle);
        } catch (DataAccessException exp) {
            throw new DataBaseHandlingException("Failed to access database when saving raffle");
        }
    }

    public List<RaffleImage> saveImages(List<RaffleImage> images) {
        try {
            return imagesRepository.saveAll(images.stream().toList());
        } catch (DataAccessException exp) {
            throw new DataBaseHandlingException("Failed to access database when saving images");
        }
    }

    public Set<RaffleDTO> getAll(String authHeader) {
        Long associationId = authClient.getId(authHeader);
        Set<Raffle> raffles = findByAssociationId(associationId);
        return mapper.fromRaffleSet(raffles);
    }

    public Set<Raffle> findByAssociationId(Long associationId) {
        try {
            return new HashSet<>(rafflesRepository.findByAssociationId(associationId));
        } catch (DataAccessException exp) {
            throw new DataBaseHandlingException("Failed to access database when retrieving raffles");
        }
    }
}
