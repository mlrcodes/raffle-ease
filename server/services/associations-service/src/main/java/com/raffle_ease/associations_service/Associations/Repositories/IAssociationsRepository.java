package com.raffle_ease.associations_service.Associations.Repositories;


import com.raffle_ease.associations_service.Associations.Models.Association;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAssociationsRepository extends JpaRepository<Association, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    boolean existsByName(String name);
}
