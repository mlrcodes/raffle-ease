package com.raffleease.raffles_service.Raffles.Model;

import com.raffleease.raffles_service.Tickets.Models.Ticket;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "Raffles")
public class Raffle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private RaffleStatus status;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @ElementCollection
    @CollectionTable(name = "raffle_photos", joinColumns = @JoinColumn(name = "raffle_id"))
    @Column(name = "photo_url")
    private Set<String> photosURLs;

    @Column(nullable = false)
    private BigDecimal ticketPrice;

    private Long availableTickets;

    private Long totalTickets;

    @OneToMany(mappedBy = "raffle", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ticket> tickets;

    @Column(nullable = false)
    private Long associationId;

}