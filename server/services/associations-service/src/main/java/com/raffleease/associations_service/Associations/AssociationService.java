package com.raffleease.associations.Associations;

import com.raffleease.associations.Associations.DTO.AssociationDTO;
import com.raffleease.associations.Associations.Mappers.AssociationMapper;
import com.raffleease.associations.Associations.Models.Association;
import com.raffleease.associations.Exceptions.CustomExceptions.AssociationNotFoundException;
import com.raffleease.associations.Exceptions.CustomExceptions.DataBaseAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AssociationService {

    private final AssociationRepository repository;

    private final AssociationMapper mapper;

    public AssociationDTO findById(Long id) {
        try {
            return this.mapper.fromAssociation(
                    this.repository.findById(id)
                            .orElseThrow(() -> new AssociationNotFoundException("Association not found"))
            );
        } catch (Exception exp) {
            throw new DataBaseAccessException("Failed to access database when retrieving association information");
        }
    }

    public Boolean exists(Long id) {
        try {
            Optional<Association> association = this.repository.findById(id);
            return association.isPresent();
        } catch (Exception exp) {
            throw new DataBaseAccessException("Failed to access database when retrieving association information");
        }
    }
}
