package com.raffleease.orders_service.Tickets.Client;

import com.raffleease.common_models.DTO.Tickets.CheckReservationRequest;
import com.raffleease.common_models.DTO.Tickets.PurchaseRequest;
import com.raffleease.common_models.DTO.Tickets.TicketDTO;
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

    @PostMapping("/purchase")
    Set<TicketDTO> purchase(
            @RequestBody PurchaseRequest purchaseRequest
    );
}