package com.raffle_ease.associations_service.Associations.Mappers;

import com.raffle_ease.associations_service.Associations.Models.Address;
import com.raffle_ease.associations_service.Associations.Models.Association;
import com.raffleease.common_models.DTO.Associations.AssociationDTO;
import com.raffleease.common_models.DTO.Kafka.AssociationCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssociationsMapper {
    private final AddressMapper addressMapper;

    public Association toAssociation(AssociationCreate DTO) {
        return Association.builder()
                .name(DTO.name())
                .email(DTO.email())
                .phoneNumber(DTO.phoneNumber())
                .address(addressMapper.toAddress(DTO))
                .build();
    }

    public AssociationDTO fromAssociation(Association association) {
        return AssociationDTO.builder()
                .id(association.getId())
                .name(association.getName())
                .email(association.getEmail())
                .phoneNumber(association.getPhoneNumber())
                .address(addressMapper.fromAddress(association.getAddress()))
                .build();
    }
}