package com.raffleease.orders_service.Orders.Mappers;

import com.raffleease.orders_service.Orders.DTO.OrderData;
import com.raffleease.orders_service.Orders.Models.Order;
import com.raffleease.orders_service.Tickets.DTO.TicketDTO;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OrdersMapper {

    public OrderData fromOrderToOrderData(Order order, Set<TicketDTO> purchasedTickets) {
        return OrderData.builder()
                .orderId(order.getId())
                .orderReference(order.getOrderReference())
                .orderDate(order.getOrderDate())
                .tickets(purchasedTickets)
                .build();
    }
}