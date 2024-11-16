package com.raffleease.raffles_service.Associations;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "associations-client",
        url = "${application.config.associations-url}"
)
public interface AssociationsClient {
    @GetMapping("/exists/{id}")
    Boolean existsById(@PathVariable("id") Long id);
}