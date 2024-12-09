package com.raffleease.auth_server.Services;

import com.raffleease.auth_server.Model.UserPrincipal;
import com.raffleease.common_models.Exceptions.CustomExceptions.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsersService usersService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            return new UserPrincipal(usersService.findByEmail(email));
        } catch (UsernameNotFoundException exp) {
            throw new AuthenticationException(exp.getMessage());
        }
    }
}