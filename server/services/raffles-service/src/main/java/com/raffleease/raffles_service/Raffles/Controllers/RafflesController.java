package com.raffleease.raffles_service.Raffles.Controllers;

import com.raffleease.raffles_service.Raffles.DTO.RaffleCreationRequest;
import com.raffleease.raffles_service.Raffles.DTO.RaffleResponse;
import com.raffleease.raffles_service.Raffles.Services.RafflesCreationService;
import com.raffleease.raffles_service.Raffles.Services.RafflesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/raffles")
public class RafflesController {
    private final RafflesCreationService raffleCreationService;
    private final RafflesService service;

    @PostMapping("/create")
    public ResponseEntity<Long> create(
            @RequestBody @Valid RaffleCreationRequest request
    ) {
        return ResponseEntity.ok(raffleCreationService.createRaffle(request));
    }

    @PostMapping("/publish/{id}")
    public ResponseEntity<Long> publish(
            @PathVariable Long id
    )  {
        return ResponseEntity.ok(service.publish(id));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<RaffleResponse> get(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.get(id));
    }

}