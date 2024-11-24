package com.raffleease.auth_server.Associations;

import com.raffleease.common_models.DTO.Associations.AssociationDTO;
import com.raffleease.common_models.DTO.Kafka.AssociationCreate;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "associations-client",
        url = "${application.config.associations-url}"
)
public interface AssociationsClient {
    @PostMapping("/create")
    AssociationDTO create(@Valid @RequestBody AssociationCreate request);
}