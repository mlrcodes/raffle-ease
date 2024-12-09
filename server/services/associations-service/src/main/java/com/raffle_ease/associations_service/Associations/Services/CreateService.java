package com.raffle_ease.associations_service.Associations.Services;

import com.raffle_ease.associations_service.Associations.Mappers.AssociationsMapper;
import com.raffle_ease.associations_service.Associations.Models.Association;
import com.raffle_ease.associations_service.Associations.Repositories.IAssociationsRepository;
import com.raffleease.common_models.DTO.Associations.AssociationDTO;
import com.raffleease.common_models.DTO.Kafka.AssociationCreate;
import com.raffleease.common_models.Exceptions.CustomExceptions.ConflictException;
import com.raffleease.common_models.Exceptions.CustomExceptions.DataBaseHandlingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
        } catch (Exception exp) {
            throw new DataBaseHandlingException("Error accessing database when saving association: " + exp.getMessage());
        }
    }

    private void validateUniqueData(AssociationCreate associationCreate) {
        if (repository.existsByName(associationCreate.name())) {
            throw new ConflictException("Association name already exists");
        }
        if (repository.existsByEmail(associationCreate.email())) {
            throw new ConflictException("Email already exists");
        }
        if (repository.existsByPhoneNumber(associationCreate.phoneNumber())) {
            throw new ConflictException("Phone Number already exists");
        }
    }
}
