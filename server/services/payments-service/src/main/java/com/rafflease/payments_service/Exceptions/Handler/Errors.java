package com.rafflease.payments_service.Exceptions.Handler;

import lombok.Builder;

import java.util.Map;

@Builder
public record Errors (
        Map<String, String> errors
) {
}