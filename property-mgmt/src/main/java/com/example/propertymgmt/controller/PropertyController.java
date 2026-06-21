package com.example.propertymgmt.controller;

import com.example.propertymgmt.dto.PropertyDtos.PropertyRequest;
import com.example.propertymgmt.model.Property;
import com.example.propertymgmt.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    public List<Property> getAll() {
        return propertyService.getAllForCurrentUser();
    }

    @GetMapping("/{id}")
    public Property getOne(@PathVariable Long id) {
        return propertyService.getOneForCurrentUser(id);
    }

    @PostMapping
    public ResponseEntity<Property> create(@Valid @RequestBody PropertyRequest request) {
        return ResponseEntity.ok(propertyService.create(request));
    }

    @PutMapping("/{id}")
    public Property update(@PathVariable Long id, @Valid @RequestBody PropertyRequest request) {
        return propertyService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        propertyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/properties/search?city=austin
    @GetMapping("/search")
    public List<Property> search(@RequestParam String city) {
        return propertyService.searchByCity(city);
    }
}
