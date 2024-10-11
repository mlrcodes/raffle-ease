package com.raffleease.raffles_service.Tickets.Controllers;

import com.raffleease.raffles_service.Tickets.DTO.*;
import com.raffleease.raffles_service.Tickets.Services.GenerateRandomService;
import com.raffleease.raffles_service.Tickets.Services.SearchService;
import com.raffleease.raffles_service.Tickets.Services.TicketsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tickets")
public class TicketsController {

    private final TicketsService service;

    private final SearchService searchService;

    private final GenerateRandomService generateRandomService;

    @PostMapping("/find-by-number")
    public ResponseEntity<TicketResponseSet> findByTicketNumber(
            @RequestBody SearchRequest request
    ) {
        return ResponseEntity.ok(searchService.findByTicketNumber(request));
    }

    @PostMapping("/generate-random")
    public ResponseEntity<TicketResponseSet> generateRandom(
            @RequestBody GenerateRandomRequest request
    ) {
        return ResponseEntity.ok(generateRandomService.generateRandom(request));
    }

    @PostMapping("/reserve")
    public ResponseEntity<Void> reserve(
            @RequestBody Set<Long> ticketsIds
    ) {
        service.reserve(ticketsIds);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/purchase")
    public ResponseEntity<Set<TicketResponse>> purchase(
            @RequestBody PurchaseRequest purchaseRequest
    ) {
        return ResponseEntity.ok(service.purchase(purchaseRequest));
    }

    @PutMapping("/release")
    public ResponseEntity<Void> release(
            @RequestBody TicketsIdsDTO request
    ) {
        service.release(request);
        return ResponseEntity.ok().build();
    }
}