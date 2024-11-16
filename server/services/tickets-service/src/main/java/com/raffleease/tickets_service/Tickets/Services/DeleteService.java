package com.raffleease.tickets_service.Tickets.Services;

import com.raffleease.common_models.DTO.Kafka.TicketsDelete;
import com.raffleease.common_models.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.tickets_service.Tickets.Repositories.ITicketsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteService {
    private ITicketsRepository ticketsRepository;

    public void delete(TicketsDelete request) {
        try {
            ticketsRepository.deleteByRaffleId(request.raffleId());
        } catch (DataAccessException exp) {
            throw new DataBaseHandlingException("An error occurred when deleting raffle tickets");
        }
    }
}
