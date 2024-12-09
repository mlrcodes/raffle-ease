package com.raffle_ease.associations_service.Configs;

import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class EurekaConfig {

    @Bean
    public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {
        EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            config.setIpAddress(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        config.setPreferIpAddress(true);
        config.setNonSecurePortEnabled(true);
        String port = System.getenv("SERVER_PORT");
        config.setNonSecurePort(port != null ? Integer.parseInt(port) : 8080);
        return config;
    }
}
