package com.example.propertymgmt.repository;

import com.example.propertymgmt.model.Property;
import com.example.propertymgmt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    // All properties belonging to a specific logged-in user.
    List<Property> findByOwner(User owner);

    // Used to check ownership before allowing update/delete -
    // makes sure user A can't modify user B's property by guessing an ID.
    Optional<Property> findByIdAndOwner(Long id, User owner);

    // Case-insensitive partial match search, scoped to one owner.
    List<Property> findByOwnerAndCityContainingIgnoreCase(User owner, String city);
}
