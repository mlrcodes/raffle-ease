package com.raffleease.raffles_service.Raffles.Model;

import com.raffleease.common_models.DTO.Raffles.RaffleStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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

    @OneToMany(mappedBy = "raffle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RaffleImage> images;

    @Column(nullable = false)
    private BigDecimal ticketPrice;

    private Long firstTicketNumber;

    private BigDecimal revenue;

    private Long totalTickets;

    private Long availableTickets;

    private Long soldTickets;

    @ElementCollection
    @CollectionTable(name = "raffle_tickets", joinColumns = @JoinColumn(name = "raffle_id"))
    @Column(name = "ticket_id")
    private Set<String> tickets;

    @Column(nullable = false)
    private Long associationId;

}