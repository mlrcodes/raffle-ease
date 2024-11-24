package com.raffleease.tickets_service.Tickets.Services;

import com.raffleease.common_models.DTO.Kafka.TicketsAvailability;
import com.raffleease.common_models.DTO.Orders.Reservation;
import com.raffleease.common_models.DTO.Tickets.ReservationRequest;
import com.raffleease.common_models.DTO.Tickets.ReservationResponse;
import com.raffleease.common_models.DTO.Tickets.TicketDTO;
import com.raffleease.common_models.Exceptions.CustomExceptions.BusinessException;
import com.raffleease.common_models.Exceptions.CustomExceptions.ObjectNotFoundException;
import com.raffleease.tickets_service.Kafka.Producer.TicketsAvailabilityProducer;
import com.raffleease.tickets_service.Tickets.Mappers.TicketsMapper;
import com.raffleease.tickets_service.Tickets.Models.Ticket;
import com.raffleease.tickets_service.Tickets.Repositories.ITicketsRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.raffleease.common_models.DTO.Kafka.TicketsAvailability.OperationType.DECREASE;
import static com.raffleease.common_models.DTO.Kafka.TicketsAvailability.OperationType.INCREASE;
import static com.raffleease.common_models.DTO.Tickets.TicketStatus.AVAILABLE;
import static com.raffleease.common_models.DTO.Tickets.TicketStatus.RESERVED;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final TicketsService ticketsService;
    private final ITicketsRepository repository;
    private final TicketsMapper mapper;
    private final TicketsAvailabilityProducer ticketsAvailabilityProducer;

    public ReservationResponse reserve(ReservationRequest request) {
        Set<Ticket> tickets = findAllById(request.ticketsIds());
        return reserveInternal(request.raffleId(), tickets);
    }

    public ReservationResponse reserve(Long raffleId, Set<Ticket> tickets) {
        return reserveInternal(raffleId, tickets);
    }

    @Transactional
    private ReservationResponse reserveInternal(Long raffleId, Set<Ticket> tickets) {
        checkTicketsAvailability(tickets);
        ReservationResponse reservationResponse = setReservationDetails(tickets);
        modifyAvailableTickets(raffleId, (long) tickets.size(), DECREASE);
        return reservationResponse;
    }

    @Transactional
    public void release(ReservationRequest request) {
        Set<Ticket> tickets = findAllById(request.ticketsIds());
        removeReservationDetails(tickets);
        modifyAvailableTickets(request.raffleId(), (long) tickets.size(), INCREASE);
    }

    private Set<Ticket> findAllById(Set<String> ticketsIds) {
        Set<Ticket> tickets = new HashSet<>(ticketsService.findAllById(ticketsIds));
        if (tickets.isEmpty() || tickets.size() < ticketsIds.size()) {
            throw new ObjectNotFoundException("One or more tickets could not be found");
        }
        return tickets;
    }

    private void modifyAvailableTickets(
            Long raffleId,
            Long quantity,
            TicketsAvailability.OperationType operationType
    ) {
        ticketsAvailabilityProducer.modifyAvailability(
                TicketsAvailability.builder()
                        .raffleId(raffleId)
                        .quantity(quantity)
                        .operationType(operationType)
                        .build()
        );
    }

    private void checkTicketsAvailability(Set<Ticket> tickets) {
        tickets.forEach(ticket -> {
            if (ticket.getStatus() != AVAILABLE) {
                throw new BusinessException("One or more tickets are not available");
            }
        });
    }

    private void removeReservationDetails(Set<Ticket> tickets) {
        tickets.forEach(ticket -> {
            ticket.setStatus(AVAILABLE);
            ticket.setReservationFlag(null);
            ticket.setReservationTime(null);
        });
        ticketsService.saveAll(tickets);
    }

    private ReservationResponse setReservationDetails(Set<Ticket> tickets) {
        String reservationFlag = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        tickets.forEach(ticket -> {
            ticket.setStatus(RESERVED);
            ticket.setReservationFlag(reservationFlag);
            ticket.setReservationTime(now);
        });
        ticketsService.saveAll(tickets);
        Set<TicketDTO> ticketDTOS = mapper.fromTicketSet(tickets);
        return ReservationResponse.builder()
                .tickets(ticketDTOS)
                .reservationFlag(reservationFlag)
                .build();
    }

    public Boolean checkReservation(Set<Reservation> reservations) {
        return reservations.stream().allMatch(reservation -> reservation.tickets().stream().allMatch(ticket ->
                ticket.status().equals(RESERVED) && ticket.reservationFlag().equals(reservation.reservationFlag())
        ));
    }

    @Scheduled(fixedRate = 600000)
    @Transactional
    public void releaseScheduled() {
        LocalDateTime reservationTime = LocalDateTime.now().minusMinutes(10);
        repository.updateStatusAndReservationFlag(reservationTime);
        List<Document> rafflesAndTicketCount = repository.findRafflesIdAndUpdatedTicketCount(reservationTime);
        rafflesAndTicketCount.forEach(raffleAndCount -> {
            Long raffleId = raffleAndCount.getLong("_id");
            Long updatedTicketCount = raffleAndCount.getInteger("count").longValue();
            modifyAvailableTickets(raffleId, updatedTicketCount, INCREASE);
        });
        repository.updateReservationTime(reservationTime);
    }
}
