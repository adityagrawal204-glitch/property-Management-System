package com.example.propertymgmt.repository;

import com.example.propertymgmt.model.Property;
import com.example.propertymgmt.model.Unit;
import com.example.propertymgmt.model.UnitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Long> {

    List<Unit> findByProperty(Property property);

    Optional<Unit> findByIdAndProperty_Owner_Id(Long id, Long ownerId);

    long countByProperty_Owner_Id(Long ownerId);

    long countByProperty_Owner_IdAndStatus(Long ownerId, UnitStatus status);

    /**
     * Custom JPQL query for the search/filter feature: find units belonging
     * to this owner, optionally filtered by city and/or rent range.
     * The ":param" syntax binds to the @Param-annotated method arguments below.
     * Passing null for cityFilter/minRent/maxRent effectively skips that filter,
     * because "x IS NULL OR column = x" short-circuits to true when x is null.
     */
    @Query("""
        SELECT u FROM Unit u
        JOIN u.property p
        WHERE p.owner.id = :ownerId
        AND (:city IS NULL OR LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%')))
        AND (:minRent IS NULL OR u.rentAmount >= :minRent)
        AND (:maxRent IS NULL OR u.rentAmount <= :maxRent)
        """)
    List<Unit> search(
            @Param("ownerId") Long ownerId,
            @Param("city") String city,
            @Param("minRent") BigDecimal minRent,
            @Param("maxRent") BigDecimal maxRent
    );
}
