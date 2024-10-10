package com.raffleease.raffles_service.Tickets.Repositories;

import com.raffleease.raffles_service.Raffles.Models.Raffle;
import com.raffleease.raffles_service.Tickets.Models.Ticket;
import com.raffleease.raffles_service.Tickets.Models.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketsRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByRaffleAndStatus(Raffle raffle, TicketStatus status);

    List<Ticket> findByRaffleAndStatusAndTicketNumberContaining(
            Raffle raffle,
            TicketStatus status,
            String ticketNumber
    );
}