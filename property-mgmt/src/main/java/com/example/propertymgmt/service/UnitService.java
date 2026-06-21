package com.example.propertymgmt.service;

import com.example.propertymgmt.dto.PropertyDtos.UnitRequest;
import com.example.propertymgmt.model.Property;
import com.example.propertymgmt.model.Unit;
import com.example.propertymgmt.model.User;
import com.example.propertymgmt.repository.UnitRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UnitService {

    private final UnitRepository unitRepository;
    private final PropertyService propertyService;
    private final CurrentUserService currentUserService;

    public UnitService(UnitRepository unitRepository, PropertyService propertyService,
                        CurrentUserService currentUserService) {
        this.unitRepository = unitRepository;
        this.propertyService = propertyService;
        this.currentUserService = currentUserService;
    }

    public List<Unit> getAllForProperty(Long propertyId) {
        Property property = propertyService.getOneForCurrentUser(propertyId); // enforces ownership
        return unitRepository.findByProperty(property);
    }

    public Unit getOneForCurrentUser(Long unitId) {
        User owner = currentUserService.getCurrentUser();
        return unitRepository.findByIdAndProperty_Owner_Id(unitId, owner.getId())
                .orElseThrow(() -> new NoSuchElementException("Unit not found"));
    }

    public Unit create(Long propertyId, UnitRequest request) {
        Property property = propertyService.getOneForCurrentUser(propertyId); // enforces ownership
        Unit unit = new Unit(
                request.getUnitNumber(), request.getBedrooms(),
                request.getBathrooms(), request.getRentAmount(), property
        );
        return unitRepository.save(unit);
    }

    public Unit update(Long unitId, UnitRequest request) {
        Unit unit = getOneForCurrentUser(unitId); // enforces ownership
        unit.setUnitNumber(request.getUnitNumber());
        unit.setBedrooms(request.getBedrooms());
        unit.setBathrooms(request.getBathrooms());
        unit.setRentAmount(request.getRentAmount());
        return unitRepository.save(unit);
    }

    public void delete(Long unitId) {
        Unit unit = getOneForCurrentUser(unitId); // enforces ownership
        unitRepository.delete(unit);
    }

    public List<Unit> search(String city, BigDecimal minRent, BigDecimal maxRent) {
        User owner = currentUserService.getCurrentUser();
        return unitRepository.search(owner.getId(), city, minRent, maxRent);
    }
}
