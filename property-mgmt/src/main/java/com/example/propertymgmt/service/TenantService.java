package com.example.propertymgmt.service;

import com.example.propertymgmt.dto.TenantDtos.TenantRequest;
import com.example.propertymgmt.model.Tenant;
import com.example.propertymgmt.model.Unit;
import com.example.propertymgmt.model.UnitStatus;
import com.example.propertymgmt.repository.TenantRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;
    private final UnitService unitService;

    public TenantService(TenantRepository tenantRepository, UnitService unitService) {
        this.tenantRepository = tenantRepository;
        this.unitService = unitService;
    }

    /**
     * "Moving a tenant in": create the Tenant record AND flip the Unit's
     * status to OCCUPIED. Both things must happen together, or the
     * dashboard's occupancy numbers would be wrong - this is exactly the
     * kind of small business rule that the service layer exists to enforce.
     */
    public Tenant moveIn(Long unitId, TenantRequest request) {
        Unit unit = unitService.getOneForCurrentUser(unitId); // enforces ownership

        if (unit.getStatus() == UnitStatus.OCCUPIED) {
            throw new IllegalStateException("Unit is already occupied - move out the current tenant first");
        }

        Tenant tenant = new Tenant(
                request.getFullName(), request.getEmail(), request.getPhone(),
                request.getLeaseStartDate(), request.getLeaseEndDate(), unit
        );
        unit.setStatus(UnitStatus.OCCUPIED);

        return tenantRepository.save(tenant);
        // Note: saving Tenant is enough - Unit's status change is persisted
        // automatically at the end of the transaction because `unit` is a
        // managed JPA entity (this is "dirty checking").
    }

    /**
     * "Moving a tenant out": delete the Tenant record and flip the Unit
     * back to VACANT.
     */
    public void moveOut(Long unitId) {
        Unit unit = unitService.getOneForCurrentUser(unitId); // enforces ownership

        Tenant tenant = tenantRepository.findByUnit_Id(unit.getId())
                .orElseThrow(() -> new NoSuchElementException("This unit has no current tenant"));

        tenantRepository.delete(tenant);
        unit.setStatus(UnitStatus.VACANT);
    }

    public Tenant getCurrentTenant(Long unitId) {
        Unit unit = unitService.getOneForCurrentUser(unitId); // enforces ownership
        return tenantRepository.findByUnit_Id(unit.getId())
                .orElseThrow(() -> new NoSuchElementException("This unit has no current tenant"));
    }
}
