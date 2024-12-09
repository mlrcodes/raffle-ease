package com.raffleease.gateway_server.Filters;

import com.raffleease.common_models.Exceptions.CustomExceptions.AuthorizationException;
import com.raffleease.gateway_server.WebClients.Clients.AuthService;
import com.raffleease.gateway_server.Configs.RouteValidator;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final RouteValidator validator;
    private final AuthService authService;

    public AuthenticationFilter(RouteValidator validator, AuthService authService) {
        super(Config.class);
        this.validator = validator;
        this.authService = authService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(AUTHORIZATION)) {
                    throw new AuthorizationException("Missing authorization header");
                }

                String authHeader = Objects.requireNonNull(exchange.getRequest().getHeaders().get(AUTHORIZATION)).get(0);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new AuthorizationException("Invalid Authorization header format");
                }

                String token = authHeader.substring(7);

                return authService.validateToken(token)
                        .then(chain.filter(exchange))
                        .onErrorResume(e -> {
                            exchange.getResponse().setStatusCode(UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        });
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {
    }
}