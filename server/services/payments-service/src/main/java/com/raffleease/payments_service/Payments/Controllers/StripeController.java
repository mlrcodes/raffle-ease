package com.raffleease.payments_service.Payments.Controllers;

import com.raffleease.common_models.DTO.Orders.CreateSessionRequest;
import com.raffleease.payments_service.Payments.Services.StripeService;
import com.raffleease.payments_service.Payments.Services.WebHookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/stripe")
public class StripeController {
    private final StripeService stripeService;
    private final WebHookService webHookService;

    @GetMapping("/public-key")
    public ResponseEntity<String> getPublicKey() {
        return ResponseEntity.ok(stripeService.getPublicKey());
    }

    @PostMapping("/create-session")
    public ResponseEntity<String> createSession(
            @RequestBody CreateSessionRequest request
    ) {
        return ResponseEntity.ok(stripeService.createSession(request));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebHook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        webHookService.handleWebHook(payload, sigHeader);
        return ResponseEntity.ok().build();
    }
}