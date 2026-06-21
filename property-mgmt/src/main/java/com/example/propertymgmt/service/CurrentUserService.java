package com.example.propertymgmt.service;

import com.example.propertymgmt.model.User;
import com.example.propertymgmt.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Small helper used by every controller that needs to know "who is making
 * this request". Spring Security stores the authenticated principal's
 * email (set by JwtAuthFilter) in the SecurityContext - this class looks
 * that email up and returns the matching User entity from our database.
 */
@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database"));
    }
}
