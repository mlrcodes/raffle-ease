package com.raffleease.payments_service.Raffles.Clients;

import com.raffleease.common_models.DTO.Raffles.RaffleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "raffles-service",
        url = "${application.config.raffles-url}"
)
public interface RafflesClient {
    @GetMapping("/get/{id}")
    RaffleDTO get(@PathVariable Long id);
}