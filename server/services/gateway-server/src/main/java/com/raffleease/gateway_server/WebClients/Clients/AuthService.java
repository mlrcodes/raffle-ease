package com.raffleease.gateway_server.WebClients.Clients;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final WebClient webClient;

    @Value("${application.config.auth-url}")
    private String authServerUrl;

    public Mono<Void> validateToken(String token) {
        return webClient.post()
                .uri(authServerUrl + "/validate")
                .bodyValue(token)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode().is4xxClientError()) {
                        return Mono.error(new RuntimeException("Token inválido o expirado"));
                    }
                    return Mono.error(new RuntimeException("Error comunicándose con el auth-server"));
                });
    }
}
