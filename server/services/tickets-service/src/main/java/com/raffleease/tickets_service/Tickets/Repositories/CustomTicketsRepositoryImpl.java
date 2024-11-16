package com.raffleease.tickets_service.Tickets.Repositories;

import com.raffleease.common_models.DTO.Kafka.TicketsRaffle;
import com.raffleease.tickets_service.Tickets.Models.Ticket;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class CustomTicketsRepositoryImpl implements CustomTicketsRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void updateStatusAndReservationFlag(LocalDateTime reservationTime) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is("RESERVED")
                .and("reservationTime").lt(reservationTime));
        Update update = new Update();
        update.set("status", "AVAILABLE");
        update.unset("reservationFlag");
        mongoTemplate.updateMulti(query, update, Ticket.class);
    }

    @Override
    public void updateReservationTime(LocalDateTime reservationTime) {
        Query query = new Query();
        query.addCriteria(Criteria.where("reservationTime").lt(reservationTime));
        Update update = new Update();
        update.unset("reservationTime");
        mongoTemplate.updateMulti(query, update, Ticket.class);
    }

    @Override
    public void setRaffle(TicketsRaffle request) {
        Query query = new Query(Criteria.where("_id").in(request.tickets()));
        Update update = new Update();
        update.set("raffleId", request.raffleId());
        mongoTemplate.updateMulti(query, update, Ticket.class);
    }

    @Override
    public List<Document> findRafflesIdAndUpdatedTicketCount(LocalDateTime threshold) {
        Criteria criteria = Criteria.where("status").is("RESERVED")
                .and("reservationTime").lt(threshold);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("raffleId").count().as("count")
        );
        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "Tickets", Document.class);
        return results.getMappedResults();
    }


}
