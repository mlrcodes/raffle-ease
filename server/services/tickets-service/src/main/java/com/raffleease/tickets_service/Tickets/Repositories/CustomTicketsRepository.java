package com.raffleease.tickets_service.Tickets.Repositories;

import com.raffleease.common_models.DTO.Kafka.TicketsRaffle;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomTicketsRepository {
    void updateStatusAndReservationFlag(LocalDateTime reservationTime);
    void updateReservationTime(LocalDateTime reservationTime);
    void setRaffle(TicketsRaffle request);
    List<Document> findRafflesIdAndUpdatedTicketCount(LocalDateTime threshold);
}
