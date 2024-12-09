package com.raffleease.auth_server.Services;

import com.raffleease.auth_server.Model.User;
import com.raffleease.auth_server.Repositories.UsersRepository;
import com.raffleease.common_models.Exceptions.CustomExceptions.DataBaseHandlingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsersService {
    private final UsersRepository repository;

    public User findByEmail(String email) {
        try {
            return repository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        } catch (Exception exp) {
            throw new DataBaseHandlingException("Error accessing database when retrieving user information: " + exp.getMessage());
        }
    }

    public User saveUser(User user) {
        try {
            return repository.save(user);
        } catch (Exception exp) {
            throw new DataBaseHandlingException("Error accessing database when saving new user: " + exp.getMessage());
        }
    }
}
