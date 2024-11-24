package com.raffle_ease.associations_service.Associations.Services;

import com.raffle_ease.associations_service.Associations.Mappers.AssociationsMapper;
import com.raffle_ease.associations_service.Associations.Models.Association;
import com.raffle_ease.associations_service.Associations.Repositories.IAssociationsRepository;
import com.raffleease.common_models.DTO.Associations.AssociationDTO;
import com.raffleease.common_models.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.common_models.Exceptions.CustomExceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AssociationsService {
    private final IAssociationsRepository repository;
    private final AssociationsMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(AssociationsService.class);

    public AssociationDTO findById(Long id) {
        try {
            return mapper.fromAssociation(
                    repository.findById(id)
                            .orElseThrow(() -> new ObjectNotFoundException("Association not found"))
            );
        } catch (Exception exp) {
            throw new DataBaseHandlingException("Failed to access database when retrieving association information");
        }
    }

    public Boolean exists(Long id) {
        try {
            Optional<Association> association = repository.findById(id);
            return association.isPresent();
        } catch (Exception exp) {
            throw new DataBaseHandlingException("Failed to access database when retrieving association information");
        }
    }
}