package com.raffleease.raffles_service.Tickets.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record CheckReservationRequest(
        @NotBlank(message = "Reservation flag is required to complete order")
        String reservationFlag,

        @NotEmpty(message = "Must purchase at least one ticket")
        Set<Long> tickets
) { }
