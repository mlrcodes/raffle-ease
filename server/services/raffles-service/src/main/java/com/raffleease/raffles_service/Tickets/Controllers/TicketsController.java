package com.raffleease.raffles_service.Tickets.Controllers;

import com.raffleease.raffles_service.Tickets.DTO.*;
import com.raffleease.raffles_service.Tickets.Services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tickets")
public class TicketsController {
    private final TicketsService service;
    private final PurchaseService purchaseService;
    private final SearchService searchService;
    private final ReservationService reservationService;
    private final GenerateRandomService generateRandomService;

    @PostMapping("/find-by-number")
    public ResponseEntity<Set<TicketResponse>> findByTicketNumber(
            @Valid @RequestBody SearchRequest request
    ) {
        return ResponseEntity.ok(searchService.findByTicketNumber(request));
    }

    @PostMapping("/generate-random")
    public ResponseEntity<Set<TicketResponse>> generateRandom(
            @Valid @RequestBody GenerateRandomRequest request
    ) {
        return ResponseEntity.ok(generateRandomService.generateRandom(request));
    }

    @PostMapping("/reserve")
    public ResponseEntity<Set<TicketResponse>> reserve(
            @Valid @RequestBody ReservationRequest request
    ) {
        return ResponseEntity.ok(reservationService.reserve(request));
    }

    @PostMapping("/purchase")
    public ResponseEntity<Set<TicketResponse>> purchase(
            @Valid @RequestBody PurchaseRequest purchaseRequest
    ) {
        return ResponseEntity.ok(purchaseService.purchase(purchaseRequest));
    }

    @PostMapping("/release")
    public ResponseEntity<Void> release(
            @Valid @RequestBody ReservationRequest request
    ) {
        reservationService.release(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check-reservation")
    public ResponseEntity<Boolean> checkReservation(
            @Valid @RequestBody CheckReservationRequest request
    ) {
        return ResponseEntity.ok(reservationService.checkReservation(request));
    }
}