package com.raffleease.auth_server.Services;

import com.raffleease.auth_server.Associations.AssociationsClient;
import com.raffleease.auth_server.Model.User;
import com.raffleease.auth_server.Repositories.UsersRepository;
import com.raffleease.common_models.DTO.Associations.AssociationDTO;
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

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository repository;
    private final AssociationsClient associationsClient;

    @Transactional
    public String register(RegisterRequest request) {
        validateUniqueEmail(request);
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
        AssociationDTO association = saveAssociation(request);
        user.setAssociationId(association.id());
        User savedUser = saveUser(user);
        return jwtService.generateToken(savedUser);
    }

    private void validateUniqueEmail(RegisterRequest request) {
        Optional<User> user = repository.findByEmail(request.email());
        if (user.isPresent()) {
            throw new AuthException("Email already exists");
        }
    }

    private AssociationDTO saveAssociation(RegisterRequest request) {
        AssociationCreate association = AssociationCreate.builder()
                .name(request.name())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .city(request.city())
                .zipCode(request.zipCode())
                .build();
        try {
            return associationsClient.create(association);
        } catch (RuntimeException e) {
            throw new AuthException("Cannot complete sign up due to an error creating association: " + e);
        }
    }

    public String authenticate(AuthRequest authRequest) {
        String email = authRequest.email();
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, authRequest.password())
        );
        if (!authenticate.isAuthenticated()) throw new AuthException("Invalid credentials");
        User user = repository.findByEmail(email).orElseThrow(() -> new AuthException("User not found with email: " + email));
        return jwtService.generateToken(user);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    public Long getId(String authHeader) {
        String token = authHeader.substring(7);
        return jwtService.getClaim(token, claims -> claims.get("id", Long.class));
    }

    private User saveUser(User user) {
        try {
            return repository.save(user);
        } catch (RuntimeException e) {
            throw new DataBaseHandlingException("Error accessing database when saving new user");
        }
    }
}
