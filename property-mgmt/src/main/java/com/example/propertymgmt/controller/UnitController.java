package com.example.propertymgmt.controller;

import com.example.propertymgmt.dto.PropertyDtos.UnitRequest;
import com.example.propertymgmt.model.Unit;
import com.example.propertymgmt.service.UnitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    // Units are nested under a property: /api/properties/3/units
    @GetMapping("/api/properties/{propertyId}/units")
    public List<Unit> getAllForProperty(@PathVariable Long propertyId) {
        return unitService.getAllForProperty(propertyId);
    }

    @PostMapping("/api/properties/{propertyId}/units")
    public ResponseEntity<Unit> create(@PathVariable Long propertyId, @Valid @RequestBody UnitRequest request) {
        return ResponseEntity.ok(unitService.create(propertyId, request));
    }

    // Direct unit access/update/delete: /api/units/7
    @GetMapping("/api/units/{unitId}")
    public Unit getOne(@PathVariable Long unitId) {
        return unitService.getOneForCurrentUser(unitId);
    }

    @PutMapping("/api/units/{unitId}")
    public Unit update(@PathVariable Long unitId, @Valid @RequestBody UnitRequest request) {
        return unitService.update(unitId, request);
    }

    @DeleteMapping("/api/units/{unitId}")
    public ResponseEntity<Void> delete(@PathVariable Long unitId) {
        unitService.delete(unitId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search/filter across all of the current user's units.
     * Example: GET /api/units/search?city=austin&minRent=1000&maxRent=2000
     * All params are optional - omitting one just skips that filter.
     */
    @GetMapping("/api/units/search")
    public List<Unit> search(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) BigDecimal minRent,
            @RequestParam(required = false) BigDecimal maxRent
    ) {
        return unitService.search(city, minRent, maxRent);
    }
}
