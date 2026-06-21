package com.example.propertymgmt.security;

import com.example.propertymgmt.model.User;
import com.example.propertymgmt.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Spring Security needs to know how to load a user by "username" (we use
 * email as the username) to check credentials. This class is the bridge
 * between our own User entity/UserRepository and Spring Security's
 * UserDetails interface, which it uses internally.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email " + email));

        // We only have one role for everyone in this simple version (ROLE_USER),
        // so we don't model roles as a separate entity yet.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(() -> "ROLE_USER")
        );
    }
}
