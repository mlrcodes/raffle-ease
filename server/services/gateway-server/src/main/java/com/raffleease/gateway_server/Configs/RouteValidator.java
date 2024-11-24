package com.raffleease.gateway_server.Configs;

import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/api/v1/auth/authenticate",
            "/api/v1/auth/register",
            "/api/v1/auth/validate",
            "/eureka",
            "/api/v1/raffles/get",
            "/api/v1/s3/get-view-url",
            "/api/v1/stripe/public-key",
            "/api/v1/tickets/find-by-number",
            "/api/v1/tickets/generate-random",
            "/api/v1/tickets/reserve",
            "api/v1/orders/create-order"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}