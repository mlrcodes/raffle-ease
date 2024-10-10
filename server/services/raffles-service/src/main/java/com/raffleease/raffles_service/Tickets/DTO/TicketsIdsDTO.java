package com.raffleease.raffles_service.Tickets.DTO;

import java.util.Set;

public record TicketsIdsDTO(
        Set<Long> tickets
) {}