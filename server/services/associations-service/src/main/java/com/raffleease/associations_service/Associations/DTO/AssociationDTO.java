package com.raffleease.associations.Associations.DTO;

import lombok.Builder;

@Builder
public record AssociationDTO (
        Long id,

        String name,

        String email,

        String phoneNumber,

        AddressDTO address

) {}
