package com.raffleease.orders_service.Exceptions.Handler;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> error
) {

}