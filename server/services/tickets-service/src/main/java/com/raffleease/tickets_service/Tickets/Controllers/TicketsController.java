package com.raffleease.tickets_service.Tickets.Controllers;

import com.raffleease.common_models.DTO.Tickets.*;
import com.raffleease.tickets_service.Tickets.Services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tickets")
public class TicketsController {
    private final TicketsService ticketsService;
    private final PurchaseService purchaseService;
    private final SearchService searchService;
    private final ReservationService reservationService;
    private final GenerateRandomService generateRandomService;

    @PostMapping("/find-by-number")
    public ResponseEntity<Set<TicketDTO>> findByTicketNumber(
            @Valid @RequestBody SearchRequest request
    ) {
        return ResponseEntity.ok(searchService.findByTicketNumber(request));
    }

    @PostMapping("/generate-random")
    public ResponseEntity<Set<TicketDTO>> generateRandom(
            @Valid @RequestBody GenerateRandomRequest request
    ) {
        return ResponseEntity.ok(generateRandomService.generateRandom(request));
    }

    @PostMapping("/reserve")
    public ResponseEntity<Set<TicketDTO>> reserve(
            @Valid @RequestBody ReservationRequest request
    ) {
        return ResponseEntity.ok(reservationService.reserve(request));
    }

    @PostMapping("/purchase")
    public ResponseEntity<Set<TicketDTO>> purchase(
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

    @PostMapping("/create")
    public ResponseEntity<Set<String>> createTickets(
            @Valid @RequestBody RaffleTicketsCreationRequest request
    ) {
        return ResponseEntity.ok(ticketsService.createTickets(request));
    };
}