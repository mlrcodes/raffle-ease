package com.raffleease.auth_server.Services;

import com.raffleease.auth_server.Kafka.Producers.AssociationCreateProducer;
import com.raffleease.auth_server.Model.User;
import com.raffleease.auth_server.Repositories.UsersRepository;
import com.raffleease.common_models.DTO.Auth.AuthRequest;
import com.raffleease.common_models.DTO.Auth.RegisterRequest;
import com.raffleease.common_models.DTO.Kafka.AssociationCreate;
import com.raffleease.common_models.Exceptions.CustomExceptions.AuthException;
import com.raffleease.common_models.Exceptions.CustomExceptions.DataBaseHandlingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository repository;
    private final AssociationCreateProducer associationCreateProducer;

    @Transactional
    public String register(RegisterRequest request) {
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
        saveAssociation(request);
        User savedUser = saveUser(user);
        return jwtService.generateToken(savedUser.getEmail());
    }

    private void saveAssociation(RegisterRequest request) {
        AssociationCreate association = AssociationCreate.builder()
                .name(request.name())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .city(request.city())
                .zipCode(request.zipCode())
                .build();
        try {
            associationCreateProducer.createAssociation(association);
        } catch (RuntimeException e) {
            throw new AuthException("Cannot complete sign up due to an error saving association: " + e);
        }
    }

    public String authenticate(AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password())
        );

        if (!authenticate.isAuthenticated()) throw new AuthException("");

        return jwtService.generateToken(authRequest.email());
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    private User saveUser(User user) {
        try {
            return repository.save(user);
        } catch (RuntimeException e) {
            throw new DataBaseHandlingException("Error accessing database when saving new user");
        }
    }
}
