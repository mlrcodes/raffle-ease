package com.raffleease.orders_service.Orders.Mappers;

import com.raffleease.common_models.DTO.Orders.OrderDTO;
import com.raffleease.common_models.DTO.Tickets.TicketDTO;
import com.raffleease.orders_service.Orders.Models.Order;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class OrdersMapper {
    public OrderDTO fromOrder(Order order, Set<TicketDTO> purchasedTickets) {
        return OrderDTO.builder()
                .orderId(order.getId())
                .orderReference(order.getOrderReference())
                .orderDate(order.getOrderDate()).
                status(order.getStatus())
                .tickets(purchasedTickets)
                .build();
    }
}