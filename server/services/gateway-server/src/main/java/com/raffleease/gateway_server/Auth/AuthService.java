package com.raffleease.gateway_server.Auth;

import com.raffleease.common_models.Exceptions.CustomExceptions.AuthException;
import com.raffleease.gateway_server.Configs.RestTemplateConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.http.HttpMethod.GET;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RestTemplate restTemplate;
    private final RestTemplateConfig restTemplateConfig;

    public void validateToken(String token) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(restTemplateConfig.getAuthServerUrl())
                    .path("/validate")
                    .queryParam("token", token)
                    .toUriString();
            ResponseEntity<Void> response = restTemplate.exchange(url, GET, null, Void.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new AuthException("Token validation failed");
            }
        } catch (Exception e) {
            throw new AuthException("Unauthorized access to application: " + e);
        }
    }
}
