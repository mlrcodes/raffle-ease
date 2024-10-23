package com.raffleease.orders_service.Orders.Services;

import com.raffleease.orders_service.Exceptions.CustomExceptions.CheckoutException;
import com.raffleease.orders_service.Exceptions.CustomExceptions.TicketPurchaseException;
import com.raffleease.orders_service.Kafka.Brokers.Producers.NotificationProducer;
import com.raffleease.orders_service.Orders.DTO.OrderRequest;
import com.raffleease.orders_service.Orders.Models.Order;
import com.raffleease.orders_service.Orders.Models.OrderStatus;
import com.raffleease.orders_service.Orders.Repositories.IOrdersRepository;
import com.raffleease.orders_service.Payments.Client.PaymentClient;
import com.raffleease.orders_service.Payments.DTO.CreateSessionRequest;
import com.raffleease.orders_service.Tickets.Client.TicketsClient;
import com.raffleease.orders_service.Tickets.DTO.CheckReservationRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class purchasesService {
    private final TicketsClient ticketsClient;
    private final PaymentClient paymentClient;
    private final IOrdersRepository repository;
    private final NotificationProducer producer;
    private static final Logger logger = LoggerFactory.getLogger(purchasesService.class);

    @Transactional
    public String createOrder(OrderRequest request) {

        logger.info("Reservation flag: " + request.reservationFlag());
        logger.info("Tickets: " + request.tickets());
        logger.info("Raffle: " + request.raffleId());
        logger.info("TIckets quantity: " + (long) request.tickets().size());

        checkReservation(request.reservationFlag(), request.tickets());
        Order order = saveOrder(request);
        return createSession(request, order.getId());
    }

    private void checkReservation(String reservationFlag, Set<Long> tickets) {
        CheckReservationRequest reservationRequest = CheckReservationRequest.builder()
                .tickets(tickets)
                .reservationFlag(reservationFlag)
                .build();
        Boolean areTicketsReserved = ticketsClient.checkReservation(reservationRequest);
        if (!areTicketsReserved) {
            throw new TicketPurchaseException("Tickets must be previously reserved to complete purchase");
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
            // releaseTickets(request);
            throw new CheckoutException("Error processing checkout: " + exp.getMessage());
        }
    }
    /*

    private void releaseTickets(OrderRequest request) {
        producer.sendTicketsReleaseNotification(
                TicketsReleaseRequest.builder()
                        .ticketsIds(request.tickets())
                        .build()
        );
    }

     */
}