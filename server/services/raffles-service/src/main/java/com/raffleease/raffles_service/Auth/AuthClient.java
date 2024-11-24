package com.raffleease.raffles_service.Auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "auth-client",
        url = "${application.config.auth-url}"
)
public interface AuthClient {
    @GetMapping("/get-id")
    Long getId(@RequestHeader("Authorization") String token);
}
