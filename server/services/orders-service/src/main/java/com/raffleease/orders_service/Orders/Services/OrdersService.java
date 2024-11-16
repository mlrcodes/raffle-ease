package com.raffleease.orders_service.Orders.Services;

import com.raffleease.common_models.DTO.Kafka.TicketsRelease;
import com.raffleease.common_models.DTO.Orders.CreateSessionRequest;
import com.raffleease.common_models.DTO.Orders.OrderRequest;
import com.raffleease.common_models.DTO.Tickets.CheckReservationRequest;
import com.raffleease.common_models.Exceptions.CustomExceptions.BusinessException;
import com.raffleease.common_models.Exceptions.CustomExceptions.CheckoutException;
import com.raffleease.orders_service.Kafka.Producers.ReleaseProducer;
import com.raffleease.orders_service.Orders.Models.Order;
import com.raffleease.orders_service.Orders.Repositories.IOrdersRepository;
import com.raffleease.orders_service.Payments.Client.PaymentClient;
import com.raffleease.orders_service.Tickets.Client.TicketsClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static com.raffleease.common_models.DTO.Orders.OrderStatus.PENDING;

@RequiredArgsConstructor
@Service
public class OrdersService {
    private final TicketsClient ticketsClient;
    private final PaymentClient paymentClient;
    private final IOrdersRepository repository;
    private final ReleaseProducer releaseProducer;

    @Transactional
    public String createOrder(OrderRequest request) {
        checkReservation(request.reservationFlag(), request.tickets());
        Order order = saveOrder(request);
        return createSession(request, order.getId());
    }

    private void checkReservation(String reservationFlag, Set<String> tickets) {
        CheckReservationRequest reservationRequest = CheckReservationRequest.builder()
                .tickets(tickets)
                .reservationFlag(reservationFlag)
                .build();
        Boolean areTicketsReserved = ticketsClient.checkReservation(reservationRequest);
        if (!areTicketsReserved) {
            throw new BusinessException("Tickets must be previously reserved to complete purchase");
        }
    }

    private Order saveOrder(OrderRequest request) {
        return repository.save(
                Order.builder()
                        .status(PENDING)
                        .orderDate(Instant.now().toEpochMilli())
                        .ticketsIds(request.tickets())
                        .orderReference(UUID.randomUUID().toString())
                        .build()
        );
    }

    private String createSession(OrderRequest request, Long orderId) {
        try {
            return paymentClient.createSession(
                    CreateSessionRequest.builder()
                            .raffleId(request.raffleId())
                            .quantity((long) request.tickets().size())
                            .orderId(orderId)
                            .build()
            );
        } catch (RuntimeException exp) {
            releaseTickets(request);
            throw new CheckoutException("Error processing checkout: " + exp.getMessage());
        }
    }

    private void releaseTickets(OrderRequest request) {
        releaseProducer.sendTicketsReleaseNotification(
                TicketsRelease.builder()
                        .ticketsIds(request.tickets())
                        .raffleId(request.raffleId())
                        .build()
        );
    }
}