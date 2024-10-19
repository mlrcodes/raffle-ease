package com.raffleease.raffles_service.Tickets.Repositories;

import com.raffleease.raffles_service.Raffles.Model.Raffle;
import com.raffleease.raffles_service.Tickets.Models.Ticket;
import com.raffleease.raffles_service.Tickets.Models.TicketStatus;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketsRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByRaffleAndStatus(Raffle raffle, TicketStatus status);

    List<Ticket> findByRaffleAndStatusAndTicketNumberContaining(
            Raffle raffle,
            TicketStatus status,
            String ticketNumber
    );

    @Modifying
    @Query("UPDATE Ticket t SET t.status = 'AVAILABLE', t.reservationFlag = null WHERE t.status = 'RESERVED' AND t.reservationTime < :reservationTime")
    void updateStatusAndReservationFlag(@Param("reservationTime") LocalDateTime reservationTime);

    @Modifying
    @Query("UPDATE Ticket t SET t.reservationTime = null WHERE t.reservationTime < :reservationTime")
    void updateReservationTime(@Param("reservationTime") LocalDateTime reservationTime);


    @Query("SELECT t.raffle, COUNT(t) FROM Ticket t WHERE t.status = 'RESERVED' AND t.reservationTime < :threshold GROUP BY t.raffle")
    List<Object[]> findRafflesAndUpdatedTicketCount(@Param("threshold") LocalDateTime threshold);
}