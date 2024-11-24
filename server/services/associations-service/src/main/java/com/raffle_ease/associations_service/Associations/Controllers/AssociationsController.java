package com.raffle_ease.associations_service.Associations.Controllers;

import com.raffle_ease.associations_service.Associations.Services.AssociationsService;
import com.raffle_ease.associations_service.Associations.Services.CreateService;
import com.raffleease.common_models.DTO.Associations.AssociationDTO;
import com.raffleease.common_models.DTO.Kafka.AssociationCreate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/associations")
public class AssociationsController {
    private final AssociationsService service;
    private final CreateService createService;

    @GetMapping("/get/{id}")
    public ResponseEntity<AssociationDTO> findById(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> exists(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(service.exists(id));
    }

    @PostMapping("/create")
    public ResponseEntity<AssociationDTO> create(
            @Valid @RequestBody AssociationCreate request
    ) {
        return ResponseEntity.ok(createService.create(request));
    }
}