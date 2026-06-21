package com.example.propertymgmt.controller;

import com.example.propertymgmt.dto.TenantDtos.TenantRequest;
import com.example.propertymgmt.model.Tenant;
import com.example.propertymgmt.service.TenantService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/units/{unitId}/tenant")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping
    public Tenant getCurrentTenant(@PathVariable Long unitId) {
        return tenantService.getCurrentTenant(unitId);
    }

    // POST /api/units/5/tenant/move-in
    @PostMapping("/move-in")
    public ResponseEntity<Tenant> moveIn(@PathVariable Long unitId, @Valid @RequestBody TenantRequest request) {
        return ResponseEntity.ok(tenantService.moveIn(unitId, request));
    }

    // POST /api/units/5/tenant/move-out
    @PostMapping("/move-out")
    public ResponseEntity<Void> moveOut(@PathVariable Long unitId) {
        tenantService.moveOut(unitId);
        return ResponseEntity.noContent().build();
    }
}
