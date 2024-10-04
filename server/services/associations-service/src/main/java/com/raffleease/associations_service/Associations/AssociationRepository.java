package com.raffleease.associations.Associations;

import com.raffleease.associations.Associations.Models.Association;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociationRepository extends JpaRepository<Association, Long> {
}
