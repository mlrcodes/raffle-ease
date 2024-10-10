package com.raffle_ease.associations_service.Associations.DTO;

import lombok.Builder;

@Builder
public record AddressDTO(
        Long id,
        String city,
        String zipCode
) {}