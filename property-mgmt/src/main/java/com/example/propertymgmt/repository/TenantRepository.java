package com.example.propertymgmt.repository;

import com.example.propertymgmt.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findByUnit_Id(Long unitId);
}
