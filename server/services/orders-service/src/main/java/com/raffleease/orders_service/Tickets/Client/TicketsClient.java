package com.raffleease.orders_service.Tickets.Client;

import com.raffleease.orders_service.Orders.DTO.PurchaseRequest;
import com.raffleease.orders_service.Tickets.DTO.CheckReservationRequest;
import com.raffleease.orders_service.Tickets.DTO.TicketDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@FeignClient(
        name = "tickets-client",
        url = "${application.config.tickets-url}"
)
public interface TicketsClient {
    @PostMapping("/check-reservation")
    Boolean checkReservation(
            @RequestBody CheckReservationRequest request
    );

    @PutMapping("/purchase")
    Set<TicketDTO> purchase(
            @RequestBody PurchaseRequest purchaseRequest
    );
}