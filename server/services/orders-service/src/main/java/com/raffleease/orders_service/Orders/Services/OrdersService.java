package com.raffleease.orders_service.Orders.Services;

import com.raffleease.common_models.DTO.Kafka.TicketsRelease;
import com.raffleease.common_models.DTO.Orders.CreateSessionRequest;
import com.raffleease.common_models.DTO.Orders.OrderRequest;
import com.raffleease.common_models.DTO.Orders.Reservation;
import com.raffleease.common_models.DTO.Tickets.TicketDTO;
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
import java.util.stream.Collectors;

import static com.raffleease.common_models.DTO.Orders.OrderStatus.PENDING;

@RequiredArgsConstructor
@Service
public class OrdersService {
    private final TicketsClient ticketsClient;
    private final PaymentClient paymentClient;
    private final IOrdersRepository repository;
    private final ReleaseProducer releaseProducer;

    @Transactional
    public String createOrder(OrderRequest orderRequest) {
        checkReservations(orderRequest.reservations());
        Set<String> allTicketIds = collectAllTickets(orderRequest.reservations());
        Order order = saveOrder(allTicketIds);
        return createSession(orderRequest.raffleId(), allTicketIds, order.getId());
    }

    private void checkReservations(Set<Reservation> reservations) {
        boolean areTicketsReserved = ticketsClient.checkReservation(reservations);
        if (!areTicketsReserved) {
            throw new BusinessException("Tickets must be previously reserved to complete purchase");
        }
    }

    private Order saveOrder(Set<String> allTicketIds) {
        return repository.save(
                Order.builder()
                        .status(PENDING)
                        .orderDate(Instant.now().toEpochMilli())
                        .ticketsIds(allTicketIds)
                        .orderReference(UUID.randomUUID().toString())
                        .build()
        );
    }

    private static Set<String> collectAllTickets(Set<Reservation> reservations) {
        return reservations.stream()
                .flatMap(reservation -> reservation.tickets().stream().map(TicketDTO::id))
                .collect(Collectors.toSet());
    }

    private String createSession(Long raffleId, Set<String> ticketIds, Long orderId) {
        try {
            return paymentClient.createSession(
                    CreateSessionRequest.builder()
                            .raffleId(raffleId)
                            .quantity((long) ticketIds.size())
                            .orderId(orderId)
                            .build()
            );
        } catch (RuntimeException exp) {
            releaseTickets(raffleId, ticketIds);
            throw new CheckoutException("Error processing checkout: " + exp.getMessage());
        }
    }

    private void releaseTickets(Long raffleId, Set<String> ticketIds) {
        releaseProducer.sendTicketsReleaseNotification(
                TicketsRelease.builder()
                        .ticketsIds(ticketIds)
                        .raffleId(raffleId)
                        .build()
        );
    }
}