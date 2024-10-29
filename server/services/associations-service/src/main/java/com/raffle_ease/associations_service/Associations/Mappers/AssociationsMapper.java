package com.raffle_ease.associations_service.Associations.Mappers;

import com.raffle_ease.associations_service.Associations.Models.Association;
import com.raffleease.common_models.DTO.Associations.AssociationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssociationsMapper {

    private final AddressMapper addressMapper;

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