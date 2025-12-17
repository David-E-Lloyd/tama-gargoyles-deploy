package com.example.tama_gargoyles.service;

import com.example.tama_gargoyles.model.User;
import com.example.tama_gargoyles.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    /**
     * MVP: uses the logged-in OIDC user's email to find (or create) a User row.
     * Requires the "email" scope.
     */
    // MVP shortcut: always return user id 1 (stoneking)
    public User getCurrentUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof OidcUser oidcUser)) {
            throw new IllegalStateException("No authenticated  user found");
        }

        String email = oidcUser.getEmail();
        if (email == null || email.isBlank()) {
            throw new IllegalStateException("Auth0 did not provide an email claim (check scopes/connection settings)");
        }

        return userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(new User(email)));


    }
}
