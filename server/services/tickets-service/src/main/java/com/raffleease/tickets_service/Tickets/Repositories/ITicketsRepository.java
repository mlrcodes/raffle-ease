package com.raffleease.tickets_service.Tickets.Repositories;

import com.raffleease.common_models.DTO.Tickets.TicketStatus;
import com.raffleease.tickets_service.Tickets.Models.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ITicketsRepository extends MongoRepository<Ticket, String>, CustomTicketsRepository {

    List<Ticket> findByRaffleIdAndStatus(Long raffleId, TicketStatus status);

    List<Ticket> findByRaffleIdAndStatusAndTicketNumberContaining(
            Long raffleId,
            TicketStatus status,
            String ticketNumber
    );

    void deleteByRaffleId(Long raffleId);

    Optional<Ticket> findTopByRaffleIdOrderByTicketNumberDesc(Long raffleId);
}
