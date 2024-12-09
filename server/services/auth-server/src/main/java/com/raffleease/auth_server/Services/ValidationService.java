package com.raffleease.auth_server.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService {
    private final JwtService jwtService;

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    public Long getId(String authHeader) {
        String token = authHeader.substring(7);
        return jwtService.getClaim(token, claims -> claims.get("id", Long.class));
    }
}
