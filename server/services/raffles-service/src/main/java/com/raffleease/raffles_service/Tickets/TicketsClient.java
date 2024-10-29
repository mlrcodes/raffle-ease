package com.raffleease.raffles_service.Tickets;

import com.raffleease.common_models.DTO.Tickets.RaffleTicketsCreationRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@FeignClient(
        name = "tickets-client",
        url = "${application.config.tickets-url}"
)
public interface TicketsClient {
    @PostMapping("/create")
    Set<String> createTickets(@Valid @RequestBody RaffleTicketsCreationRequest request);
}