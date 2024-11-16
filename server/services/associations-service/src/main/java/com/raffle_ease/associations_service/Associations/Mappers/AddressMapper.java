package com.raffle_ease.associations_service.Associations.Mappers;

import com.raffle_ease.associations_service.Associations.Models.Address;
import com.raffleease.common_models.DTO.Associations.AddressDTO;
import com.raffleease.common_models.DTO.Kafka.AssociationCreate;
import org.springframework.stereotype.Service;

@Service
public class AddressMapper {

    public AddressDTO fromAddress(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .city(address.getCity())
                .zipCode(address.getZipCode())
                .build();
    }

    public Address toAddress(AssociationCreate associationCreate) {
        return Address.builder()
                .city(associationCreate.city())
                .zipCode(associationCreate.zipCode())
                .build();
    }
}