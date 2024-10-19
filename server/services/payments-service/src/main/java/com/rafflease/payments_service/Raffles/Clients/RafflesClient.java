package com.rafflease.payments_service.Raffles.Clients;

import com.rafflease.payments_service.Raffles.DTO.RaffleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "raffles-service",
        url = "${application.config.raffles-url}"
)
public interface RafflesClient {
    @GetMapping("/get/{id}")
    RaffleDTO getRaffleInfo(@PathVariable Long id);
}