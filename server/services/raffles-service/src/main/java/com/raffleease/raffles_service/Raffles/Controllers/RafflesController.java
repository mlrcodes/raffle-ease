package com.raffleease.raffles_service.Raffles.Controllers;

import com.raffleease.common_models.DTO.Raffles.CreateRaffle;
import com.raffleease.common_models.DTO.Raffles.EditRaffle;
import com.raffleease.common_models.DTO.Raffles.RaffleDTO;
import com.raffleease.raffles_service.Raffles.Services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/raffles")
public class RafflesController {
    private final CreateService createService;
    private final RafflesService service;
    private final StatusService statusService;
    private final DeleteService deleteService;
    private final EditService editService;

    @PostMapping("/create")
    public ResponseEntity<RaffleDTO> create(
            @Valid @RequestBody CreateRaffle request,
            @RequestHeader("Authorization") String authHeader
    ) {
        return ResponseEntity.ok(createService.createRaffle(request, authHeader));
    }

    @PutMapping("/publish/{id}")
    public ResponseEntity<RaffleDTO> publish(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(statusService.publish(id));
    }

    @PutMapping("/pause/{id}")
    public ResponseEntity<RaffleDTO> pause(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(statusService.pause(id));
    }

    @PutMapping("/restart/{id}")
    public ResponseEntity<RaffleDTO> restart(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(statusService.restart(id));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<RaffleDTO> edit(
            @PathVariable Long id,
            @Valid @RequestBody EditRaffle editRaffle
    ) {
        return ResponseEntity.ok(editService.edit(id, editRaffle));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<RaffleDTO> get(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping("/get-all/")
    public ResponseEntity<Set<RaffleDTO>> getAll(
            @RequestHeader("Authorization") String authHeader
    ) {
        return ResponseEntity.ok(service.getAll(authHeader));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        deleteService.delete(id);
        return ResponseEntity.ok().build();
    }

}