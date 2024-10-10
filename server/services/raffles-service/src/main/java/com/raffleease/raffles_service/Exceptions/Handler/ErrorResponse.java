package com.raffleease.raffles_service.Exceptions.Handler;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) { }