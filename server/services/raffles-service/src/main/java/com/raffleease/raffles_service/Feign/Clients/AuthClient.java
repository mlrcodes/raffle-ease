package com.raffleease.raffles_service.Feign.Clients;

import com.raffleease.raffles_service.Configs.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "auth-client",
        url = "${application.config.auth-url}",
        configuration = FeignConfig.class
)
public interface AuthClient {
    @GetMapping("/get-id")
    Long getId(@RequestHeader("Authorization") String authHeader);
}
