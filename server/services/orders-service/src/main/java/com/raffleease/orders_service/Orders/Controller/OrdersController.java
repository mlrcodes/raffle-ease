package com.raffleease.orders_service.Orders.Controller;

import com.raffleease.orders_service.Orders.DTO.OrderRequest;
import com.raffleease.orders_service.Orders.Services.purchasesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController {
    private final purchasesService purchasesService;

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(
            @RequestBody @Valid OrderRequest request
    ) {
        return ResponseEntity.ok(purchasesService.createOrder(request));
    }
}