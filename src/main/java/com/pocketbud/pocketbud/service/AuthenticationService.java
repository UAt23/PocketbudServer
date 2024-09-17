package com.pocketbud.pocketbud.service;

import com.pocketbud.pocketbud.model.User;
import com.pocketbud.pocketbud.repository.UserRepository;
import com.pocketbud.pocketbud.config.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtUtils jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    public String authenticate(String username, String password) throws AuthenticationException {
        System.out.println("Attempting authentication for user: " + username);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            if (authentication.isAuthenticated()) {
                System.out.println("Authentication successful for user: " + username);
                return jwtUtil.generateToken(username);
            } else {
                System.out.println("Authentication failed for user: " + username);
                throw new AuthenticationException("Authentication failed") {};
            }

        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("Invalid username or password.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
