package com.example.propertymgmt.service;

import com.example.propertymgmt.dto.PropertyDtos.PropertyRequest;
import com.example.propertymgmt.model.Property;
import com.example.propertymgmt.model.User;
import com.example.propertymgmt.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final CurrentUserService currentUserService;

    public PropertyService(PropertyRepository propertyRepository, CurrentUserService currentUserService) {
        this.propertyRepository = propertyRepository;
        this.currentUserService = currentUserService;
    }

    public List<Property> getAllForCurrentUser() {
        User owner = currentUserService.getCurrentUser();
        return propertyRepository.findByOwner(owner);
    }

    public Property getOneForCurrentUser(Long id) {
        User owner = currentUserService.getCurrentUser();
        return propertyRepository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new NoSuchElementException("Property not found"));
    }

    public Property create(PropertyRequest request) {
        User owner = currentUserService.getCurrentUser();
        Property property = new Property(
                request.getName(), request.getAddressLine(), request.getCity(),
                request.getState(), request.getZipCode(), owner
        );
        return propertyRepository.save(property);
    }

    public Property update(Long id, PropertyRequest request) {
        Property property = getOneForCurrentUser(id); // also enforces ownership
        property.setName(request.getName());
        property.setAddressLine(request.getAddressLine());
        property.setCity(request.getCity());
        property.setState(request.getState());
        property.setZipCode(request.getZipCode());
        return propertyRepository.save(property);
    }

    public void delete(Long id) {
        Property property = getOneForCurrentUser(id); // also enforces ownership
        propertyRepository.delete(property);
    }

    public List<Property> searchByCity(String city) {
        User owner = currentUserService.getCurrentUser();
        return propertyRepository.findByOwnerAndCityContainingIgnoreCase(owner, city);
    }
}
