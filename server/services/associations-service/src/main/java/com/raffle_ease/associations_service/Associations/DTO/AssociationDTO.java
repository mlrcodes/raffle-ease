package com.raffle_ease.associations_service.Associations.DTO;

import lombok.Builder;

@Builder
public record AssociationDTO (
        Long id,

        String name,

        String email,

        String phoneNumber,

        AddressDTO address

) {}