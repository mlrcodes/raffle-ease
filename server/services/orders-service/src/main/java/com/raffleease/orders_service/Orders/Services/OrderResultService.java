package com.raffleease.orders_service.Orders.Services;
import com.raffleease.orders_service.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.orders_service.Exceptions.CustomExceptions.OrderNotFoundException;
import com.raffleease.orders_service.Kafka.Brokers.Producers.NotificationProducer;
import com.raffleease.orders_service.Orders.Mappers.OrdersMapper;
import com.raffleease.orders_service.Orders.Models.Order;
import com.raffleease.orders_service.Orders.Models.OrderStatus;
import com.raffleease.orders_service.Orders.Repositories.IOrdersRepository;
import com.raffleease.orders_service.Tickets.Client.TicketsClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderResultService {

    private final IOrdersRepository repository;
    private final OrdersMapper mapper;
    private final TicketsClient ticketsClient;
    private final NotificationProducer producer;
    private static final Logger logger = LoggerFactory.getLogger(OrderResultService.class);


    /*
    @Transactional
    public void handleOrderSuccess(
            SuccessNotification request
    ) {
        logger.info("HANDLING ORDER SUCCESS NOTIFICATION");
        Order order = findById(request.orderId());
        logger.info("ORDER FOUND: {}",order.getId());
        updateStatus(order, OrderStatus.COMPLETED);
        logger.info("ORDER STATUS UPDATED: {}", order.getStatus());
        Set<TicketDTO> purchasedTickets = purchaseTickets(
                order.getTicketsIds(),
                request.customerData().stripeId()
        );
        logger.info("TICKETS PURCHASED: {}", purchasedTickets);
        OrderData orderData = mapper.fromOrderToOrderData(order, purchasedTickets);
        logger.info("ORDER DATA: {}", orderData);
        notifyPaymentSuccess(
                request.paymentData(),
                request.customerData(),
                orderData
        );
        logger.info("NOTIFICATION SUCCESSFULLY SENT");
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

     */

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
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }
}