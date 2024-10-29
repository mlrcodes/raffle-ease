package com.raffleease.tickets_service.Tickets.Repositories;

import com.raffleease.tickets_service.Confgis.TestsConfig;
import com.raffleease.tickets_service.Tickets.Models.Ticket;
import lombok.*;
import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.raffleease.common_models.DTO.Tickets.TicketStatus.AVAILABLE;
import static com.raffleease.common_models.DTO.Tickets.TicketStatus.RESERVED;
import static com.raffleease.common_models.DTO.Tickets.TicketStatus.SOLD;
import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
@DataMongoTest
class ITicketsRepositoryTest {

    @Autowired
    private ITicketsRepository ticketsRepository;

    private Ticket ticket1;
    private Ticket ticket2;
    private Ticket ticket3;
    private Ticket ticket4;
    private Ticket ticket5;

    @BeforeAll
    public static void loadEnvironments() throws IOException {
        TestsConfig.loadEnvironment();
    }

    @BeforeEach
    void setup() {
        ticketsRepository.deleteAll();
        ticket1 = Ticket.builder()
                .id("1")
                .raffleId(1L)
                .status(AVAILABLE)
                .ticketNumber("1")
                .reservationFlag("abc")
                .reservationTime(LocalDateTime.of(2024, 10, 28, 9, 50, 0))
                .build();

        ticket2 = Ticket.builder()
                .id("2")
                .raffleId(1L)
                .status(RESERVED)
                .ticketNumber("2")
                .reservationFlag("bcd")
                .reservationTime(LocalDateTime.of(2024, 10, 28, 9, 50, 0))
                .build();

        ticket3 = Ticket.builder()
                .id("3")
                .raffleId(1L)
                .status(SOLD)
                .ticketNumber("3")
                .reservationFlag("cdf")
                .reservationTime(LocalDateTime.of(2024, 10, 28, 9, 50, 0))
                .build();

        ticket4 = Ticket.builder()
                .id("4")
                .raffleId(2L)
                .status(RESERVED)
                .ticketNumber("4")
                .reservationFlag("efg")
                .reservationTime(LocalDateTime.of(2024, 10, 28, 9, 50, 0))
                .build();

        ticket5 = Ticket.builder()
                .id("5")
                .raffleId(1L)
                .status(RESERVED)
                .ticketNumber("10")
                .reservationFlag("ghi")
                .reservationTime(LocalDateTime.of(2024, 10, 28, 9, 50, 0))
                .build();

        ticketsRepository.saveAll(List.of(ticket1, ticket2, ticket3, ticket4, ticket5));
    }

    @Test
    void testFindByRaffleIdAndStatusWithMultipleTickets() {
        List<Ticket> reservedTickets = ticketsRepository.findByRaffleIdAndStatus(1L, RESERVED);
        assertThat(reservedTickets).hasSize(2);
        assertThat(reservedTickets).containsExactlyInAnyOrder(ticket2, ticket5);
    }

    @Test
    void testFindByRaffleIdAndStatusAndTicketNumberContainingWithMultipleMatches() {
        List<Ticket> filteredTickets = ticketsRepository.findByRaffleIdAndStatusAndTicketNumberContaining(
                1L, RESERVED, "1");
        assertThat(filteredTickets).hasSize(1);
        assertThat(filteredTickets.get(0)).isEqualTo(ticket5);
    }

    @Test
    void testFindByRaffleIdAndStatus() {
        List<Ticket> availableTickets = ticketsRepository.findByRaffleIdAndStatus(1L, AVAILABLE);
        assertThat(availableTickets).hasSize(1);
        assertThat(availableTickets.get(0)).isEqualTo(ticket1);
    }

    @Test
    void testFindByRaffleIdAndStatusAndTicketNumberContaining() {
        List<Ticket> filteredTickets = ticketsRepository.findByRaffleIdAndStatusAndTicketNumberContaining(
                1L, AVAILABLE, "1");
        assertThat(filteredTickets).hasSize(1);
        assertThat(filteredTickets).containsExactlyInAnyOrder(ticket1);
    }

    @Test
    void testFindRafflesIdAndUpdatedTicketCountForMultipleRaffles() {
        LocalDateTime threshold = LocalDateTime.of(2024, 11, 27, 11, 0, 0);
        List<Document> results = ticketsRepository.findRafflesIdAndUpdatedTicketCount(threshold);

        assertThat(results).hasSize(2);

        Document result1 = results.stream().filter(doc -> doc.getLong("_id").equals(1L)).findFirst().orElse(null);
        Document result2 = results.stream().filter(doc -> doc.getLong("_id").equals(2L)).findFirst().orElse(null);

        assertThat(result1).isNotNull();
        assertThat(result1.getInteger("count")).isEqualTo(2);

        assertThat(result2).isNotNull();
        assertThat(result2.getInteger("count")).isEqualTo(1);
    }

    @Test
    void testUpdateStatusAndReservationFlag() {
        LocalDateTime threshold = LocalDateTime.of(2024, 10, 28, 10, 40, 0);
        ticketsRepository.updateStatusAndReservationFlag(threshold);

        List<Ticket> tickets = ticketsRepository.findAll();
        Ticket updatedTicket = tickets.stream()
                .filter(t -> t.getId().equals("2"))
                .findFirst()
                .orElse(null);

        assertThat(updatedTicket).isNotNull();
        assertThat(updatedTicket.getStatus()).isEqualTo(AVAILABLE);
        assertThat(updatedTicket.getReservationFlag()).isNull();
    }

    @Test
    void testUpdateStatusAndReservationFlagDoesNotAffectFutureReservations() {
        LocalDateTime threshold = LocalDateTime.of(2024, 10, 27, 9, 0, 0);
        ticketsRepository.updateStatusAndReservationFlag(threshold);

        List<Ticket> tickets = ticketsRepository.findAll();
        Ticket unaffectedTicket = tickets.stream()
                .filter(t -> t.getId().equals("5"))
                .findFirst()
                .orElse(null);

        assertThat(unaffectedTicket).isNotNull();
        assertThat(unaffectedTicket.getStatus()).isEqualTo(RESERVED);
    }

    @Test
    void testUpdateReservationTime() {
        LocalDateTime threshold = LocalDateTime.of(2024, 10, 28, 10, 40, 0);
        ticketsRepository.updateReservationTime(threshold);

        List<Ticket> tickets = ticketsRepository.findAll();
        Ticket updatedTicket = tickets.stream()
                .filter(t -> t.getId().equals("2"))
                .findFirst()
                .orElse(null);

        assertThat(updatedTicket).isNotNull();
        assertThat(updatedTicket.getReservationTime()).isNull();
    }

    @Test
    void testFindRafflesIdAndUpdatedTicketCount() {
        LocalDateTime threshold = LocalDateTime.of(2024, 10, 28, 10, 40, 0);
        List<Document> results = ticketsRepository.findRafflesIdAndUpdatedTicketCount(threshold);
        assertThat(results).hasSize(2);
        Document result1 = results.get(0);
        assertThat(result1.getLong("_id")).isEqualTo(1L);
        assertThat(result1.getInteger("count")).isEqualTo(2);

        Document result2 = results.get(1);
        assertThat(result2.getLong("_id")).isEqualTo(2L);
        assertThat(result2.getInteger("count")).isEqualTo(1);
    }

    @Test
    void testUpdateStatusAndReservationFlagNoChangeIfNoExpiredReservations() {
        LocalDateTime threshold = LocalDateTime.of(2024, 10, 26, 8, 0, 0);
        ticketsRepository.updateStatusAndReservationFlag(threshold);

        List<Ticket> tickets = ticketsRepository.findAll();
        Ticket unaffectedTicket = tickets.stream()
                .filter(t -> t.getId().equals("2"))
                .findFirst()
                .orElse(null);

        assertThat(unaffectedTicket).isNotNull();
        assertThat(unaffectedTicket.getStatus()).isEqualTo(RESERVED);
        assertThat(unaffectedTicket.getReservationFlag()).isEqualTo("bcd");
    }

}
