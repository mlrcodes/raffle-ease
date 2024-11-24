package com.raffle_ease.associations_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AssociationsServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AssociationsServiceApplication.class, args);
	}

}
