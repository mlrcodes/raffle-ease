package com.raffleease.orders_service.Orders.Models;

import com.raffleease.common_models.DTO.Orders.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderReference;

    @Column(nullable = false)
    private OrderStatus status;

    @ElementCollection
    @CollectionTable(name = "order_tickets", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "ticket_id")
    private Set<String> ticketsIds;

    @Column(nullable = false)
    private Long orderDate;
}