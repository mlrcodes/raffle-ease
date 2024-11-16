package com.raffleease.raffles_service.Raffles.Mappers;

import com.raffleease.common_models.DTO.Raffles.RaffleImageDTO;
import com.raffleease.raffles_service.Raffles.Model.Raffle;
import com.raffleease.raffles_service.Raffles.Model.RaffleImage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImagesMapper {
    public List<String> fromImages(List<RaffleImage> images) {
        return images.stream()
                .map(RaffleImage::getKey)
                .collect(Collectors.toList());
    }

    public List<RaffleImage> toImages(List<String> keys, Raffle raffle) {
        return keys.stream()
                .map(key -> RaffleImage.builder()
                        .raffle(raffle)
                        .key(key)
                        .build())
                .collect(Collectors.toList());
    }
}
