package com.raffleease.tickets_service.Tickets.Repositories;

import com.raffleease.common_models.DTO.Kafka.TicketsRaffleRequest;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomTicketsRepository {
    void updateStatusAndReservationFlag(LocalDateTime reservationTime);
    void updateReservationTime(LocalDateTime reservationTime);
    void setRaffle(TicketsRaffleRequest request);
    List<Document> findRafflesIdAndUpdatedTicketCount(LocalDateTime threshold);
}
