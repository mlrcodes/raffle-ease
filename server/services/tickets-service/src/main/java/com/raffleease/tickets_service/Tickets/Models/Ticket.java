package com.raffleease.tickets_service.Tickets.Models;

import com.raffleease.common_models.DTO.Tickets.TicketStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "Tickets")
public class Ticket {
    @Id
    private String id;
    private Long raffleId;
    private String ticketNumber;
    private BigDecimal price;
    private TicketStatus status;
    private String reservationFlag;
    private LocalDateTime reservationTime;
    private String customerId;
}