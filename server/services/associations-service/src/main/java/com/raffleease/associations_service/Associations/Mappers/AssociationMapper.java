package com.raffleease.associations.Associations.Mappers;

import com.raffleease.associations.Associations.DTO.AssociationDTO;
import com.raffleease.associations.Associations.Models.Association;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssociationMapper {

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
