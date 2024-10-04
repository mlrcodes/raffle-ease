package com.raffleease.associations.Associations;

import com.raffleease.associations.Associations.DTO.AssociationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/associations")
public class AssociationController {
    private final AssociationService service;

    @GetMapping("/find/{id}")
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
