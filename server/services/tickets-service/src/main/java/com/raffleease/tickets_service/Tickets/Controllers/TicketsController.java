package com.raffleease.tickets_service.Tickets.Controllers;

import com.raffleease.common_models.DTO.Orders.Reservation;
import com.raffleease.common_models.DTO.Tickets.*;
import com.raffleease.tickets_service.Tickets.Services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<List<TicketDTO>> findByTicketNumber(
            @Valid @RequestBody SearchRequest request
    ) {
        return ResponseEntity.ok(searchService.findByTicketNumber(request));
    }

    @PostMapping("/generate-random")
    public ResponseEntity<ReservationResponse> generateRandom(
            @Valid @RequestBody GenerateRandomRequest request
    ) {
        return ResponseEntity.ok(generateRandomService.generateRandom(request));
    }

    @PostMapping("/reserve")
    public ResponseEntity<ReservationResponse> reserve(
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
            @Valid @RequestBody Set<Reservation> reservations
    ) {
        return ResponseEntity.ok(reservationService.checkReservation(reservations));
    }

    @PostMapping("/create")
    public ResponseEntity<Set<String>> createTickets(
            @Valid @RequestBody RaffleTicketsCreationRequest request
    ) {
        return ResponseEntity.ok(ticketsService.createTickets(request));
    };

    @GetMapping("/get-highest/{raffleId}")
    public ResponseEntity<String> getHighestTicketNumber(
            @PathVariable("raffleId") Long raffleId
    ) {
        return ResponseEntity.ok(searchService.getHighestTicketNumber(raffleId));
    }
}