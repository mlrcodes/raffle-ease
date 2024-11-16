package com.raffleease.raffles_service.Raffles.Repositories;

import com.raffleease.raffles_service.Raffles.Model.RaffleImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ImagesRepository extends JpaRepository<RaffleImage, Long>{
}