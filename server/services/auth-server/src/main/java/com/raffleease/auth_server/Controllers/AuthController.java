package com.raffleease.auth_server.Controllers;

import com.raffleease.auth_server.Services.AuthService;
import com.raffleease.auth_server.Services.RegisterService;
import com.raffleease.auth_server.Services.ValidationService;
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
    private final RegisterService registerService;
    private final ValidationService validationService;

    @PostMapping("/register")
    public String register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return registerService.register(request);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(
            @Valid @RequestBody AuthRequest authRequest
    ) {
        return ResponseEntity.ok(authService.authenticate(authRequest));
    }

    @PostMapping("/validate")
    public ResponseEntity<Void> validateToken(
            @RequestBody String token
    ) {
        validationService.validateToken(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-id")
    public ResponseEntity<Long> getId(
            @RequestHeader("Authorization") String authHeader
    ) {
        return ResponseEntity.ok(validationService.getId(authHeader));
    }
}