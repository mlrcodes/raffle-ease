package com.raffleease.raffles_service.Validations;

import com.raffleease.raffles_service.Tickets.DTO.RaffleTicketsCreationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TicketsLimitsValidator implements ConstraintValidator<ValidTicketsLimits, RaffleTicketsCreationRequest> {

    @Override
    public boolean isValid(RaffleTicketsCreationRequest request, ConstraintValidatorContext context) {
        Long lowerLimit = request.lowerLimit();
        Long upperLimit = request.upperLimit();

        if (lowerLimit == null || upperLimit == null) {
            return true;
        }
        return upperLimit > lowerLimit;
    }
}