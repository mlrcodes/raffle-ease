package com.raffleease.orders_service.Tickets.Client;

import com.raffleease.orders_service.Orders.DTO.PurchaseRequest;
import com.raffleease.orders_service.Tickets.DTO.TicketDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@FeignClient(
        name = "raffles-service",
        url = "${application.config.tickets-url}"
)
public interface TicketsClient {
    @PutMapping("/reserve")
    void reserve(
            @RequestBody Set<Long> ticketsIds
    );

    @PutMapping("/purchase")
    Set<TicketDTO> purchase(
            @RequestBody PurchaseRequest purchaseRequest
    );
}