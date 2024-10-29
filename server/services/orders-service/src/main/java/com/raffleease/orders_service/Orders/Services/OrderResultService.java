package com.raffleease.orders_service.Orders.Services;
import com.raffleease.common_models.DTO.Customers.CustomerDTO;
import com.raffleease.common_models.DTO.Kafka.OrderSuccess;
import com.raffleease.common_models.DTO.Kafka.PaymentFailure;
import com.raffleease.common_models.DTO.Kafka.PaymentSuccess;
import com.raffleease.common_models.DTO.Kafka.TicketsReleaseRequest;
import com.raffleease.common_models.DTO.Orders.OrderDTO;
import com.raffleease.common_models.DTO.Orders.OrderStatus;
import com.raffleease.common_models.DTO.Payment.PaymentDTO;
import com.raffleease.common_models.DTO.Tickets.PurchaseRequest;
import com.raffleease.common_models.DTO.Tickets.TicketDTO;
import com.raffleease.common_models.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.common_models.Exceptions.CustomExceptions.NotificationException;
import com.raffleease.common_models.Exceptions.CustomExceptions.ObjectNotFoundException;
import com.raffleease.orders_service.Kafka.Producers.ReleaseProducer;
import com.raffleease.orders_service.Kafka.Producers.SuccessProducer;
import com.raffleease.orders_service.Orders.Mappers.OrdersMapper;
import com.raffleease.orders_service.Orders.Models.Order;
import com.raffleease.orders_service.Orders.Repositories.IOrdersRepository;
import com.raffleease.orders_service.Tickets.Client.TicketsClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.raffleease.common_models.DTO.Orders.OrderStatus.COMPLETED;

@RequiredArgsConstructor
@Service
public class OrderResultService {
    private final IOrdersRepository repository;
    private final OrdersMapper mapper;
    private final TicketsClient ticketsClient;
    private final SuccessProducer successProducer;
    private final ReleaseProducer releaseProducer;

    @Transactional
    public void handleOrderSuccess(
            PaymentSuccess request
    ) {
        Order order = findById(request.orderId());
        updateStatus(order, COMPLETED);
        Set<TicketDTO> purchasedTickets = purchaseTickets(
                order.getTicketsIds(),
                request.customer().stripeId()
        );
        OrderDTO orderData = mapper.fromOrder(order, purchasedTickets);
        notifyPaymentSuccess(
                request.payment(),
                request.customer(),
                orderData
        );
    }

    @Transactional
    public void handleOrderFailure(PaymentFailure request) {
        Order order = findById(request.orderId());
        updateStatus(order, request.status());
        releaseTickets(order.getTicketsIds(), request.raffleId());
    }

    private Set<TicketDTO> purchaseTickets(
            Set<String> ticketsIds,
            String customerId
    ) {
        return ticketsClient.purchase(
                PurchaseRequest.builder()
                        .customerId(customerId)
                        .ticketsIds(ticketsIds)
                        .build()
        );
    }

    private void notifyPaymentSuccess(
            PaymentDTO payment,
            CustomerDTO customer,
            OrderDTO order
    ) {
        try {
            successProducer.sendSuccessNotification(
                    OrderSuccess.builder()
                            .customer(customer)
                            .order(order)
                            .payment(payment)
                            .build()
            );
        } catch (RuntimeException ex) {
            throw new NotificationException("Order success notification failed");
        }
    }

    private void releaseTickets(Set<String> ticketsIds, Long raffleId) {
        try {
            releaseProducer.sendTicketsReleaseNotification(
                    TicketsReleaseRequest.builder()
                            .ticketsIds(ticketsIds)
                            .raffleId(raffleId)
                            .build()
            );
        } catch (RuntimeException ex) {
            throw new NotificationException("Order success notification failed");
        }
    }

    public void updateStatus(Order order, OrderStatus status) {
        order.setStatus(status);
        try {
            this.repository.save(order);
        } catch (DataAccessException exp) {
            throw new DataBaseHandlingException("Failed to update tickets status: " + exp.getMessage());
        }
    }

    public Order findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Order not found"));
    }
}