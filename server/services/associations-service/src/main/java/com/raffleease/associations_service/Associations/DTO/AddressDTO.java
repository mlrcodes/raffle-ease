package com.raffleease.associations.Associations.DTO;

import lombok.Builder;

@Builder
public record AddressDTO(
        Long id,
        String city,
        String zipCode
) {}
