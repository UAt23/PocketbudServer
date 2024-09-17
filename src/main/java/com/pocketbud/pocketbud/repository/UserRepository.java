package com.pocketbud.pocketbud.repository;

import com.pocketbud.pocketbud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Find a user by their username
    User findByUsername(String username);

    // Find a user by their email
    Optional<User> findByEmail(String email);

    // Check if a username already exists
    boolean existsByUsername(String username);

    // Check if an email already exists
    boolean existsByEmail(String email);
}
