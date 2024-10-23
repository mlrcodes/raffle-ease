package org.raffleease.common_models.DTO;

import lombok.Builder;

@Builder
public record MessageDTO(
        String key1,
        String key2
) {}
