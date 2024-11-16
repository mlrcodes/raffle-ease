package com.raffleease.auth_server.Repositories;

import com.raffleease.auth_server.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {
    public Optional<User> findByEmail(String email);

}