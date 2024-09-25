package com.pocketbud.pocketbud.repository;

import com.pocketbud.pocketbud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Find a user by their username
    User findByUsername(String username);

    // Find a user by their email
    Optional<User> findByEmail(String email);

    // Check if a username already exists
    boolean existsByUsername(String username);

    // Check if an email already exists
    boolean existsByEmail(String email);
}
