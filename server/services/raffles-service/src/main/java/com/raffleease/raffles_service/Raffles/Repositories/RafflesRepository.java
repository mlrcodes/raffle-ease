package com.raffleease.raffles_service.Raffles.Repositories;

import com.raffleease.raffles_service.Raffles.Model.Raffle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RafflesRepository extends JpaRepository<Raffle, Long> {
    @Query("SELECT r.ticketPrice FROM Raffle r WHERE id = :id")
    Optional<String> getStripePriceIdById(Long id);
}