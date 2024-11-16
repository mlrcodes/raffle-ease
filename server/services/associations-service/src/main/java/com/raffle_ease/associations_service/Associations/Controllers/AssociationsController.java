package com.raffle_ease.associations_service.Associations.Controllers;

import com.raffle_ease.associations_service.Associations.Services.AssociationsService;
import com.raffleease.common_models.DTO.Associations.AssociationDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/associations")
public class AssociationsController {
    private final AssociationsService service;

    @GetMapping("/get/{id}")
    public ResponseEntity<AssociationDTO> findById(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(service.exists(id));
    }
}