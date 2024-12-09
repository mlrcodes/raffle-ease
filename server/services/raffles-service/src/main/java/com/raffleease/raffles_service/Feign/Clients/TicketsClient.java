package com.raffleease.raffles_service.Feign.Clients;

import com.raffleease.common_models.DTO.Tickets.RaffleTicketsCreationRequest;
import com.raffleease.raffles_service.Configs.FeignConfig;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@FeignClient(
        name = "tickets-client",
        url = "${application.config.tickets-url}",
        configuration = FeignConfig.class
)
public interface TicketsClient {
    @PostMapping("/create")
    Set<String> createTickets(@Valid @RequestBody RaffleTicketsCreationRequest request);
}