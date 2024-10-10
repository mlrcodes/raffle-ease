package com.raffleease.raffles_service.Tickets.Models;

import com.raffleease.raffles_service.Raffles.Models.Raffle;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "Tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raffle_id")
    private Raffle raffle;

    @Column(nullable = false)
    private String ticketNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

    private String customerId;
}