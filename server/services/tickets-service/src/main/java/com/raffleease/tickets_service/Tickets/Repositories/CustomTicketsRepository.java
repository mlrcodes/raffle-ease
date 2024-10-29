package com.raffleease.tickets_service.Tickets.Repositories;

import org.bson.Document;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomTicketsRepository {
    void updateStatusAndReservationFlag(LocalDateTime reservationTime);
    void updateReservationTime(LocalDateTime reservationTime);
    List<Document> findRafflesIdAndUpdatedTicketCount(LocalDateTime threshold);
}
