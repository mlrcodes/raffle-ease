package com.raffleease.auth_server.Services;

import com.raffleease.auth_server.Model.User;
import com.raffleease.common_models.DTO.Auth.AuthRequest;
import com.raffleease.common_models.Exceptions.CustomExceptions.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsersService usersService;

    public String authenticate(AuthRequest authRequest) {
        String email = authRequest.email();
        authenticateCredentials(email, authRequest.password());
        User user = findUserByEmail(email);
        return jwtService.generateToken(user);
    }

    private void authenticateCredentials(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (org.springframework.security.core.AuthenticationException exp) {
            throw new AuthenticationException("Authentication failed. Bad credentials.");
        }
    }

    private User findUserByEmail(String email) {
        try {
            return usersService.findByEmail(email);
        } catch (UsernameNotFoundException exp) {
            throw new AuthenticationException(exp.getMessage());
        }
    }
}
