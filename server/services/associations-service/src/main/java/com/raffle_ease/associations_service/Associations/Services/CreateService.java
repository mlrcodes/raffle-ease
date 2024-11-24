package com.raffle_ease.associations_service.Associations.Services;

import com.raffle_ease.associations_service.Associations.Mappers.AssociationsMapper;
import com.raffle_ease.associations_service.Associations.Models.Association;
import com.raffle_ease.associations_service.Associations.Repositories.IAssociationsRepository;
import com.raffleease.common_models.DTO.Associations.AssociationDTO;
import com.raffleease.common_models.DTO.Kafka.AssociationCreate;
import com.raffleease.common_models.Exceptions.CustomExceptions.AuthException;
import com.raffleease.common_models.Exceptions.CustomExceptions.DataBaseHandlingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateService {
    private final IAssociationsRepository repository;
    private final AssociationsMapper mapper;

    @Transactional
    public AssociationDTO create(AssociationCreate associationCreate) {
        validateUniqueData(associationCreate);
        Association association = mapper.toAssociation(associationCreate);
        Association savedAssociation = save(association);
        return mapper.fromAssociation(savedAssociation);
    }

    private Association save(Association association) {
        try {
            return repository.save(association);
        } catch (DataAccessException e) {
            throw new DataBaseHandlingException("Error accessing database when saving association: " + e);
        }
    }

    private void validateUniqueData(AssociationCreate associationCreate) {
        if (repository.existsByName(associationCreate.name())) {
            throw new AuthException("Association name already exists");
        }
        if (repository.existsByEmail(associationCreate.email())) {
            throw new AuthException("Email already exists");
        }
        if (repository.existsByPhoneNumber(associationCreate.phoneNumber())) {
            throw new AuthException("Phone Number already exists");
        }
    }
}
