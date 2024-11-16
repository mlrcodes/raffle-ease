package com.raffleease.gateway_server.Configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
public class RestTemplateConfig {
    @Value("${application.config.auth-url}")
    private String authServerUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
