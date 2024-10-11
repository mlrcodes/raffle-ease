package com.raffleease.orders_service.Orders.Services;
import com.raffleease.orders_service.Customers.DTO.CustomerDTO;
import com.raffleease.orders_service.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.orders_service.Exceptions.CustomExceptions.NotificationException;
import com.raffleease.orders_service.Exceptions.CustomExceptions.OrderNotFoundException;
import com.raffleease.orders_service.Kafka.Messages.Failure.FailureNotification;
import com.raffleease.orders_service.Kafka.Messages.Success.SuccessNotification;
import com.raffleease.orders_service.Kafka.Messages.Success.SuccessNotificationRequest;
import com.raffleease.orders_service.Kafka.Brokers.NotificationProducer;
import com.raffleease.orders_service.Kafka.Messages.Tickets.TicketsReleaseRequest;
import com.raffleease.orders_service.Orders.DTO.OrderData;
import com.raffleease.orders_service.Orders.DTO.PurchaseRequest;
import com.raffleease.orders_service.Orders.Mappers.OrdersMapper;
import com.raffleease.orders_service.Orders.Models.Order;
import com.raffleease.orders_service.Orders.Models.OrderStatus;
import com.raffleease.orders_service.Orders.Repositories.OrderRepository;
import com.raffleease.orders_service.Payments.DTO.PaymentData;
import com.raffleease.orders_service.Tickets.Client.TicketsClient;
import com.raffleease.orders_service.Tickets.DTO.TicketDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class OrderResultService {

    private final OrderRepository repository;
    private final OrdersMapper mapper;
    private final TicketsClient ticketsClient;
    private final NotificationProducer producer;

    @Transactional
    public void handleOrderSuccess(
            SuccessNotification request
    ) {
        Order order = findById(request.orderId());
        updateStatus(order, OrderStatus.COMPLETED);
        Set<TicketDTO> purchasedTickets = purchaseTickets(
                order.getTicketsIds(),
                request.customerData().stripeId()
        );
        OrderData orderData = mapper.fromOrderToOrderData(order, purchasedTickets);
        notifyPaymentSuccess(
                request.paymentData(),
                request.customerData(),
                orderData
        );
    }

    @Transactional
    public void handleOrderFailure(FailureNotification request) {
        Order order = findById(request.orderId());
        updateStatus(order, request.status());
        releaseTickets(order.getTicketsIds());
    }

    private Set<TicketDTO> purchaseTickets(
            Set<Long> ticketsIds,
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
            PaymentData paymentData,
            CustomerDTO customerData,
            OrderData orderData
    ) {
        try {
            producer.sendSuccessNotification(
                    SuccessNotificationRequest.builder()
                            .customerData(customerData)
                            .orderData(orderData)
                            .paymentData(paymentData)
                            .build()
            );
        } catch (RuntimeException ex) {
            throw new NotificationException("Order success notification failed");
        }
    }

    private void releaseTickets(Set<Long> ticketsIds) {
        try {
            producer.sendTicketsReleaseNotification(
                    TicketsReleaseRequest.builder()
                            .ticketsIds(ticketsIds)
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

    public Order findById(Integer id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }
}