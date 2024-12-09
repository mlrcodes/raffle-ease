package com.raffleease.orders_service.Orders.Services;

import com.raffleease.common_models.DTO.Kafka.TicketsRelease;
import com.raffleease.common_models.DTO.Orders.CreateSessionRequest;
import com.raffleease.common_models.DTO.Orders.OrderRequest;
import com.raffleease.common_models.DTO.Orders.Reservation;
import com.raffleease.common_models.DTO.Tickets.TicketDTO;
import com.raffleease.common_models.Exceptions.CustomExceptions.BusinessException;
import com.raffleease.common_models.Exceptions.CustomExceptions.CheckoutException;
import com.raffleease.common_models.Exceptions.CustomExceptions.CustomStripeException;
import com.raffleease.orders_service.Kafka.Producers.ReleaseProducer;
import com.raffleease.orders_service.Orders.Models.Order;
import com.raffleease.orders_service.Orders.Repositories.IOrdersRepository;
import com.raffleease.orders_service.Feign.Clients.PaymentClient;
import com.raffleease.orders_service.Feign.Clients.TicketsClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Set;
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
        if (!ticketsClient.checkReservation(reservations)) {
            throw new BusinessException("Tickets must be previously reserved to complete purchase");
        }
    }

    private Order saveOrder(Set<String> allTicketIds) {
        String orderReference = "O-" + Instant.now().getEpochSecond() + "-" + (int) (Math.random() * 10000);
        return repository.save(
                Order.builder()
                        .status(PENDING)
                        .orderDate(Instant.now().toEpochMilli())
                        .ticketsIds(allTicketIds)
                        .orderReference(orderReference)
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
        } catch (Exception exp) {
            releaseTickets(raffleId, ticketIds);
            throw exp;
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