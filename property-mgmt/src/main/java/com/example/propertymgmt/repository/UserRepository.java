package com.example.propertymgmt.repository;

import com.example.propertymgmt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA generates the SQL for this just from the method name:
    // "SELECT * FROM app_user WHERE email = ?"
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
