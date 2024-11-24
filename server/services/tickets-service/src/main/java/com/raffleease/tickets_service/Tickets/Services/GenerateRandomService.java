package com.raffleease.tickets_service.Tickets.Services;

import com.raffleease.common_models.DTO.Tickets.GenerateRandomRequest;
import com.raffleease.common_models.DTO.Tickets.ReservationResponse;
import com.raffleease.common_models.DTO.Tickets.TicketDTO;
import com.raffleease.common_models.Exceptions.CustomExceptions.BusinessException;
import com.raffleease.common_models.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.tickets_service.Tickets.Mappers.TicketsMapper;
import com.raffleease.tickets_service.Tickets.Models.Ticket;
import com.raffleease.tickets_service.Tickets.Repositories.ITicketsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.raffleease.common_models.DTO.Tickets.TicketStatus.AVAILABLE;

@RequiredArgsConstructor
@Service
public class GenerateRandomService {
    private final ReservationService reservationService;
    private final ITicketsRepository repository;
    private final TicketsMapper mapper;

    public ReservationResponse generateRandom(GenerateRandomRequest request) {
        Set<Ticket> availableTickets = findAvailableTickets(request.raffleId());
        validateTicketAvailability(availableTickets, request.quantity());
        Set<Ticket> selectedTickets = selectRandomTickets(availableTickets, request.quantity());
        return reserveTickets(request.raffleId(), selectedTickets);
    }

    private Set<Ticket> findAvailableTickets(Long raffleId) {
        try {
            return new HashSet<>(repository.findByRaffleIdAndStatus(raffleId, AVAILABLE));
        } catch (DataAccessException exp) {
            throw new DataBaseHandlingException("Error when retrieving tickets info");
        }
    }

    private void validateTicketAvailability(Set<Ticket> availableTickets, Long requestedQuantity) {
        if (availableTickets.isEmpty() || availableTickets.size() < requestedQuantity) {
            throw new BusinessException("Not enough tickets were found for this order");
        }
    }

    private ReservationResponse reserveTickets(Long raffleId, Set<Ticket> tickets) {
        return reservationService.reserve(raffleId, tickets);
    }

    private Set<Ticket> selectRandomTickets(Set<Ticket> availableTickets, Long quantity) {
        List<Ticket> tickets = new ArrayList<>(availableTickets);
        Collections.shuffle(tickets);
        tickets = tickets.subList(0, quantity.intValue());
        return new HashSet<>(tickets);
    }
}