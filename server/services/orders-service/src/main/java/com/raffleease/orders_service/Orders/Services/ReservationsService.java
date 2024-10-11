package com.raffleease.orders_service.Orders.Services;

import com.raffleease.orders_service.Exceptions.CustomExceptions.CheckoutException;
import com.raffleease.orders_service.Exceptions.CustomExceptions.TicketPurchaseException;
import com.raffleease.orders_service.Kafka.Brokers.NotificationProducer;
import com.raffleease.orders_service.Kafka.Messages.Tickets.TicketsReleaseRequest;
import com.raffleease.orders_service.Orders.DTO.OrderRequest;
import com.raffleease.orders_service.Orders.Models.Order;
import com.raffleease.orders_service.Orders.Models.OrderStatus;
import com.raffleease.orders_service.Orders.Repositories.OrderRepository;
import com.raffleease.orders_service.Payments.Client.PaymentClient;
import com.raffleease.orders_service.Payments.DTO.CreateSessionRequest;
import com.raffleease.orders_service.Payments.DTO.CreateSessionResponse;
import com.raffleease.orders_service.Tickets.Client.TicketsClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ReservationsService {

    private final TicketsClient ticketsClient;

    private final PaymentClient paymentClient;

    private final OrderRepository repository;

    private final NotificationProducer producer;

    @Transactional
    public CreateSessionResponse reserve(OrderRequest request) {
        reserveTickets(request.tickets());
        Order order = saveOrder(request);
        return createSession(request, order.getId());
    }

    private void reserveTickets(Set<Long> tickets) {
        try {
            ticketsClient.reserve(tickets);
        } catch (RuntimeException exp) {
            throw new TicketPurchaseException("Failed to reserve tickets: " + exp.getMessage());
        }
    }

    private Order saveOrder(OrderRequest request) {
        return repository.save(
                Order.builder()
                        .status(OrderStatus.PENDING)
                        .orderDate(Instant.now().toEpochMilli())
                        .ticketsIds(request.tickets())
                        .build()
        );
    }

    private CreateSessionResponse createSession(OrderRequest request, Long orderId) {
        try {
            return paymentClient.createSession(
                    CreateSessionRequest.builder()
                            .raffleId(request.raffleId())
                            .amount((long) request.tickets().size())
                            .orderId(orderId)
                            .build()
            );
        } catch (RuntimeException exp) {
            releaseTickets(request);
            throw new CheckoutException("Error processing checkout: " + exp.getMessage());
        }
    }

    private void releaseTickets(OrderRequest request) {
        producer.sendTicketsReleaseNotification(
                TicketsReleaseRequest.builder()
                        .ticketsIds(request.tickets())
                        .build()
        );
    }

}