package com.raffleease.auth_server.Controllers;

import com.raffleease.auth_server.Services.AuthService;
import com.raffleease.common_models.DTO.Auth.AuthRequest;
import com.raffleease.common_models.DTO.Auth.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public String register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return authService.register(request);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(
            @Valid @RequestBody AuthRequest authRequest
    ) {
        return ResponseEntity.ok(authService.authenticate(authRequest));
    }

    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-id")
    public ResponseEntity<Long> getId(
            @RequestHeader("Authorization") String authHeader
    ) {
        return ResponseEntity.ok(authService.getId(authHeader));
    }
}