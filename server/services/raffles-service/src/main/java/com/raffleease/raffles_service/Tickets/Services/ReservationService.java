package com.raffleease.raffles_service.Tickets.Services;

import com.raffleease.raffles_service.Exceptions.CustomExceptions.BusinessException;
import com.raffleease.raffles_service.Raffles.Model.Raffle;
import com.raffleease.raffles_service.Raffles.Services.RafflesService;
import com.raffleease.raffles_service.Tickets.DTO.CheckReservationRequest;
import com.raffleease.raffles_service.Tickets.DTO.ReservationRequest;
import com.raffleease.raffles_service.Tickets.DTO.TicketResponse;
import com.raffleease.raffles_service.Tickets.Mappers.TicketsMapper;
import com.raffleease.raffles_service.Tickets.Models.Ticket;
import com.raffleease.raffles_service.Tickets.Models.TicketStatus;
import com.raffleease.raffles_service.Tickets.Repositories.TicketsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReservationService {
    private final TicketsService ticketsService;
    private final RafflesService rafflesService;
    private final TicketsRepository repository;
    private final TicketsMapper mapper;

    public Set<TicketResponse> reserve(ReservationRequest request) {
        Raffle raffle = rafflesService.findById(request.raffleId());
        Set<Ticket> tickets = findAllById(request.ticketsIds());
        return reserveInternal(raffle, tickets);
    }

    public Set<TicketResponse> reserve(Raffle raffle, Set<Ticket> tickets) {
        return reserveInternal(raffle, tickets);
    }

    @Transactional
    private Set<TicketResponse> reserveInternal(Raffle raffle, Set<Ticket> tickets) {
        checkTicketsAvailability(tickets);
        Set<Ticket> reservedTickets = setReservationDetails(tickets);
        rafflesService.reduceAvailableTickets(raffle, tickets.size());
        return mapper.fromTicketSetToTicketResponseSet(reservedTickets);
    }

    @Transactional
    public void release(ReservationRequest request) {
        Raffle raffle = rafflesService.findById(request.raffleId());
        Set<Ticket> tickets = findAllById(request.ticketsIds());
        removeReservationDetails(tickets);
        rafflesService.increaseAvailableTickets(raffle, tickets.size());
    }

    private Set<Ticket> findAllById(Set<Long> ticketsIds) {
        Set<Ticket> tickets = new HashSet<>(ticketsService.findAllById(ticketsIds));
        if (tickets.isEmpty() || tickets.size() < ticketsIds.size()) {
            throw new BusinessException("One or more tickets could not be found");
        }
        return tickets;
    }

    private void checkTicketsAvailability(Set<Ticket> tickets) {
        tickets.forEach(ticket -> {
            if (ticket.getStatus() != TicketStatus.AVAILABLE) {
                throw new BusinessException("One or more tickets are not available");
            }
        });
    }

    private void removeReservationDetails(Set<Ticket> tickets) {
        tickets.forEach(ticket -> {
            ticket.setStatus(TicketStatus.AVAILABLE);
            ticket.setReservationFlag(null);
            ticket.setReservationTime(null);
        });
        ticketsService.saveAll(tickets);
    }

    private Set<Ticket> setReservationDetails(Set<Ticket> tickets) {
        String reservationFlag = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        tickets.forEach(ticket -> {
            ticket.setStatus(TicketStatus.RESERVED);
            ticket.setReservationFlag(reservationFlag);
            ticket.setReservationTime(now);
        });
        return ticketsService.saveAll(tickets);
    }

    public Boolean checkReservation(CheckReservationRequest request) {
        Set<Ticket> tickets = findAllById(request.tickets());
        return tickets.stream().allMatch(ticket ->
            ticket.getStatus().equals(TicketStatus.RESERVED) && ticket.getReservationFlag().equals(request.reservationFlag())
        );
    }

    @Scheduled(fixedRate = 600000)
    @Transactional
    public void releaseScheduled() {
        LocalDateTime reservationTime = LocalDateTime.now().minusMinutes(10);
        repository.updateStatusAndReservationFlag(reservationTime);
        List<Object[]> rafflesAndTicketCount = repository.findRafflesAndUpdatedTicketCount(reservationTime);
        rafflesAndTicketCount.forEach(raffleAndCount -> {
            Raffle raffle = (Raffle) raffleAndCount[0];
            long updatedTicketCount = (long) raffleAndCount[1];
            rafflesService.increaseAvailableTickets(raffle, updatedTicketCount);
        });
        repository.updateReservationTime(reservationTime);
    }


}
