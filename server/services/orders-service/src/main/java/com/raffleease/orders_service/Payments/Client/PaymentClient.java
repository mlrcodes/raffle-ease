package com.raffleease.orders_service.Payments.Client;

import com.raffleease.orders_service.Payments.DTO.CreateSessionRequest;
import com.raffleease.orders_service.Payments.DTO.CreateSessionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "payment-client",
        url = "${application.config.stripe-url}"
)
public interface PaymentClient {
    @PostMapping("/create-session")
    CreateSessionResponse createSession(
            @RequestBody CreateSessionRequest request
    );
}