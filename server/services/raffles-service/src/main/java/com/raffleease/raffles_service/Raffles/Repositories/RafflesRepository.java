package com.raffleease.raffles_service.Raffles.Repositories;

import com.raffleease.raffles_service.Raffles.Model.Raffle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RafflesRepository extends JpaRepository<Raffle, Long> {
    List<Raffle> findByAssociationId(Long associationId);

}