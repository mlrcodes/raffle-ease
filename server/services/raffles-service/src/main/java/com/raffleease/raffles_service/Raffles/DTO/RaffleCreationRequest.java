package com.raffleease.raffles_service.Raffles.DTO;


import com.raffleease.raffles_service.Tickets.DTO.RaffleTicketsCreationRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Set;

public record RaffleCreationRequest(

        @NotBlank(message = "Must provide a title")
        String title,

        @Size(max = 500, message = "Description can have at most 500 characters")
        String description,

        @Future(message = "End date must be in the future")
        LocalDateTime endDate,

        @Size(max = 10, message = "A maximum of 10 photos are allowed")
        Set<String> photosURLs,

        @Valid
        RaffleTicketsCreationRequest ticketsInfo,

        @NotNull(message = "Association ID is required")
        @Positive(message = "Association ID must be a positive number")
        Long associationId

) {
}
