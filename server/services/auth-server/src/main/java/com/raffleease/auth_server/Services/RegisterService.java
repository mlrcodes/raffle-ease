package com.raffleease.auth_server.Services;

import com.raffleease.auth_server.Feign.Clients.AssociationsClient;
import com.raffleease.auth_server.Model.User;
import com.raffleease.common_models.DTO.Associations.AssociationDTO;
import com.raffleease.common_models.DTO.Auth.RegisterRequest;
import com.raffleease.common_models.DTO.Kafka.AssociationCreate;
import com.raffleease.common_models.Exceptions.CustomExceptions.ConflictException;
import com.raffleease.common_models.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.common_models.Exceptions.CustomExceptions.DependencyException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RegisterService {
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AssociationsClient associationsClient;
    private final UsersService usersService;

    @Transactional
    public String register(RegisterRequest request) {
        validateUniqueEmail(request.email());
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
        AssociationDTO association = saveAssociation(request);
        user.setAssociationId(association.id());
        User savedUser = usersService.saveUser(user);
        return jwtService.generateToken(savedUser);
    }

    private void validateUniqueEmail(String email) {
        try {
            User user = usersService.findByEmail(email);
            if (user != null) {
                throw new ConflictException("Email <" + email + "> already exists");
            }
        } catch (DataBaseHandlingException ignore) {}
    }

    private AssociationDTO saveAssociation(RegisterRequest request) {
        return associationsClient.create(AssociationCreate.builder()
                .name(request.name())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .city(request.city())
                .zipCode(request.zipCode())
                .build()
        );
    }
}
