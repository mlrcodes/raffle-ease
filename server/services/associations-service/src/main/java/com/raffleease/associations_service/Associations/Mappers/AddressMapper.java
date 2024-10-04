package com.raffleease.associations.Associations.Mappers;

import com.raffleease.associations.Associations.DTO.AddressDTO;
import com.raffleease.associations.Associations.Models.Address;
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
}
